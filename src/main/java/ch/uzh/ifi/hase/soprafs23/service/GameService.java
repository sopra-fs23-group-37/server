package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerStatus;
import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final RoundService roundService;

    @Autowired
    public GameService(
            @Qualifier("userRepository") UserRepository userRepository,
            @Qualifier("gameRepository") GameRepository gameRepository,
            RoundService roundService) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.roundService = roundService;
    }

    public List<Game> getPublicGames() {
        List<Game> allGames = this.gameRepository.findAll();
        return allGames;
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
        if (playerId == game.getHost().getUserId()) {
            game.setHostStatus(PlayerStatus.CONNECTED);
        } else if (playerId == game.getGuest().getUserId()) {
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

        // return the updated game
        return game;
    }

    public Game startGame(Long gameId) throws IOException, InterruptedException {

        Game foundGame = gameRepository.findByGameId(gameId);
        if (foundGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The game with the gameId " + gameId + " does not exist!");
        }
        if (foundGame.getHostStatus() == PlayerStatus.DISCONNECTED) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The host was not found!");
        }
        if (foundGame.getGuestStatus() == PlayerStatus.DISCONNECTED) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The guest was not found!");
        }

        // update the game status
        Game game = getGame(gameId);
        game = setStartingPlayer(game);

        // start the first round
        game.setCurrentRound(roundService.newRound(game));
        game.setTotalRounds(game.getTotalRounds() + 1);

        // update the game status
        game.setGameStatus(GameStatus.ONGOING);

        // save to repo and flush
        game = gameRepository.save(game);
        gameRepository.flush();

        return game;
    }

    public Game setStartingPlayer(Game game) {

        // TODO: write a proper method to set the starting player
        game.setStartingPlayer(Role.HOST);

        // save to repo and flush
        game = gameRepository.save(game);
        gameRepository.flush();
        return game;
    }

    public RoundService getRoundService() {
        return this.roundService;
    }

}