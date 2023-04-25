package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerStatus;
import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerMoveMessage;
import ch.uzh.ifi.hase.soprafs23.entity.Round;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.WSDTOMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Game Service
 * This class is the "worker" and responsible for all functionality related to
 * the game
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(LoginService.class);

    private static final Random rnd = new Random();

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final RoundService roundService;
    private final MoveLogicService moveLogicService;
    private final WebsocketService websocketService;

    public GameService(@Qualifier("userRepository") UserRepository userRepository,
            @Qualifier("gameRepository") GameRepository gameRepository, RoundService roundService,
            MoveLogicService moveLogicService, WebsocketService websocketService) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.roundService = roundService;
        this.moveLogicService = moveLogicService;
        this.websocketService = websocketService;
    }

    public List<Game> getPublicGames() {
        List<Game> openGames = this.gameRepository.findByGameStatus(GameStatus.WAITING);
        return openGames;
    }

    public Game createGame(Game newGame) {
        // update Session status
        newGame.setGameStatus(GameStatus.CREATED);
        newGame.setCreatedDate(new Date());

        // find host
        String baseErrorMessage = "Host with id %x was not found";
        Long hostId = newGame.getHost().getUserId();

        User host = userRepository.findByUserId(hostId);
        if (host == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, hostId));
        }

        // set host to user
        newGame.setHost(host);
        newGame.setCreatedDate(new Date());
        newGame.setHostStatus(PlayerStatus.WAITING);
        newGame.setGuestStatus(PlayerStatus.WAITING);
        newGame.setTotalRounds(0);

        // save to repo and flush
        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        log.debug("Created Information for Session: {}", newGame);
        return newGame;
    }

    public Game getGame(Long gameId) {
        return this.gameRepository.findByGameId(gameId);
    }

    public Game joinGame(Long guestId) {

        // find the player who wants to join a game
        User guest = userRepository.findByUserId(guestId);

        // throw error if guest is not a valid user
        String playerErrorMessage = "Player with id %x was not found";
        if (guest == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format(playerErrorMessage, guestId));
        }

        // get open games and pick oldest one
        List<Game> waitingGames = this.gameRepository.findByGameStatus(GameStatus.WAITING);
        Game nextGame = waitingGames.isEmpty() ? null : waitingGames.get(0);

        // throw errror if no waiting games
        String gameErrorMessage = "There is no open game to join. Try creating your own game.";
        if (nextGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format(gameErrorMessage));
        }

        // add guest to game
        nextGame.setGuest(guest);
        nextGame.setGameStatus(GameStatus.GUEST_SET);

        // save to repo and flush
        nextGame = gameRepository.save(nextGame);
        gameRepository.flush();

        return nextGame;
    }

    public Game websocketJoin(Long gameId, Long playerId) throws IOException, InterruptedException {
        // get the correct game
        Game game = getGame(gameId);

        // update the host/guest status in the game
        if (playerId.equals(game.getHost().getUserId())) {
            game.setHostStatus(PlayerStatus.CONNECTED);
        } else if (playerId.equals(game.getGuest().getUserId())) {
            game.setGuestStatus(PlayerStatus.CONNECTED);
        }

        // update the game status
        if (game.getHostStatus().equals(PlayerStatus.CONNECTED)
                && game.getGuestStatus().equals(PlayerStatus.CONNECTED)) {
            game.setGameStatus(GameStatus.CONNECTED);
            // startGame(game);
        } else if (game.getHostStatus().equals(
                PlayerStatus.CONNECTED) || game.getGuestStatus().equals(PlayerStatus.CONNECTED)) {
            game.setGameStatus(GameStatus.WAITING);
        }

        // save to repo and flush
        game = gameRepository.save(game);
        gameRepository.flush();

        // send the game update as dto via the lobby channel
        websocketService.sendToLobby(gameId, WSDTOMapper.INSTANCE.convertEntityToWSGameStatusDTO(game));

        return game;
    }

    public Game startGame(Long gameId, Long playerId) throws IOException, InterruptedException {

        // update the game status
        Game game = getGame(gameId);

        // if the guest is trying to start the game, just return the game as is
        if (game.getGuest().getUserId().equals(playerId)) {
            return game;
        }

        game.setGuestPoints(0);
        game.setHostPoints(0);
        game = setStartingPlayer(game);

        // start the first round
        game.setCurrentRound(roundService.newRound(game));
        game.setTotalRounds(game.getTotalRounds() + 1);

        // update the game status
        game.setGameStatus(GameStatus.ONGOING);

        // save to repo and flush
        gameRepository.save(game);
        gameRepository.flush();

        // send the game update as dto via the Game channel
        websocketService.sendToGame(gameId, WSDTOMapper.INSTANCE.convertEntityToWSGameStatusDTO(game));
        websocketService.sendRoundUpdate(game, game.getCurrentRound());

        return game;
    }

    public Game setStartingPlayer(Game game) {

        if (rnd.nextBoolean()) {
            game.setStartingPlayer(Role.HOST);
        } else {
            game.setStartingPlayer(Role.GUEST);
        }

        // save to repo and flush
        game = gameRepository.save(game);
        gameRepository.flush();
        return game;
    }

    public Game makeMove(long gameId, PlayerMoveMessage message) throws IOException, InterruptedException {
        Game game = getGame(gameId);

        // check needed to see if cards are even in hand / field?
        Boolean validMove = moveLogicService.checkMove(message);
        if (!validMove) {
            // handle the issue if the move is not valid and return
            return game;
        }

        // execute the move and update the round
        Round updatedRound = roundService.executeMove(game.getCurrentRound(), message);
        game.setCurrentRound(updatedRound);

        // send the updated round to the players
        websocketService.sendRoundUpdate(game, game.getCurrentRound());

        // do post move checks and determine if the round was finished
        Boolean endOfRound = roundService.postMoveChecks(updatedRound);

        // if the round was finished, updates the points and check if there is a winner
        if (endOfRound) {
            updatePoints(game);
            checkWinner(game);
        }

        // update the game in the repository and return it
        game = gameRepository.save(game);
        gameRepository.flush();
        return game;
    }

    public void updatePoints(Game game) {
        // add points from the finished round (still set as current) to the total points
        // on the game
        game.setGuestPoints(game.getGuestPoints() + game.getCurrentRound().getGuestPoints());
        game.setHostPoints(game.getHostPoints() + game.getCurrentRound().getHostPoints());

        // send the game update as dto via the Game channel
        websocketService.sendToGame(game.getGameId(), WSDTOMapper.INSTANCE.convertEntityToWSGameStatusDTO(game));
    }

    public void checkWinner(Game game) throws IOException, InterruptedException {
        // check if there is a winner, i.e. if one of the players has at least 11 points
        // and 2 more than the other player
        if ((game.getGuestPoints() >= 11 || game.getHostPoints() >= 11)
                && java.lang.Math.abs(game.getGuestPoints() - game.getHostPoints()) >= 2) {

            // check if it is the guest or the host who won and set them as the winnre
            if (game.getGuestPoints() > game.getHostPoints()) {
                game.setWinner(game.getGuest());
            } else {
                game.setWinner(game.getHost());
            }

            // update the game status
            game.setGameStatus(GameStatus.FINISHED);

            // send the game update as dto via the Game channel
            websocketService.sendToGame(game.getGameId(), WSDTOMapper.INSTANCE.convertEntityToWSGameStatusDTO(game));

        }
        // if there was no winner yet, set up a new round for the game
        else {
            game.setCurrentRound(roundService.newRound(game));
        }
    }
}