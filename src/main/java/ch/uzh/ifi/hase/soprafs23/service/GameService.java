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
import ch.uzh.ifi.hase.soprafs23.rest.mapper.WebSockDTOMapper;

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
    private final UserService userService;

    public GameService(@Qualifier("userRepository") UserRepository userRepository,
            @Qualifier("gameRepository") GameRepository gameRepository, RoundService roundService,
            MoveLogicService moveLogicService, WebsocketService websocketService, UserService userService) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.roundService = roundService;
        this.moveLogicService = moveLogicService;
        this.websocketService = websocketService;
        this.userService = userService;
    }

    public List<Game> getPublicGames() {
        List<Game> openGames = this.gameRepository.findByGameStatusAndIsPrivate(GameStatus.WAITING, false);
        return openGames;
    }

    public void sendPublicGamesUpdate() {
        websocketService.sendGamesUpdateToHome();
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

        // if game is private generate code
        if (newGame.getIsPrivate()) {
            int number = rnd.nextInt(999999);
            newGame.setGameCode(String.format("%06d", number));
        }

        // save to repo and flush
        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        log.debug("Created Information for Session: {}", newGame);
        return newGame;
    }

    public Game getGame(Long gameId) {
        return this.gameRepository.findByGameId(gameId);
    }

    public Game joinGameByCode(String gameCode, Long guestId) {
        Game gameByCode = gameRepository.findByGameCode(gameCode);
        User guest = userRepository.findByUserId(guestId);

        String playerErrorMessage = "Player with id %x was not found";
        if (guest == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format(playerErrorMessage, guestId));
        }

        String gameErrorMessage = "Invalid code. Try creating your own game.";
        if (gameByCode == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format(gameErrorMessage));
        }

        String joinErrorMessage = "Game is already full";
        if (gameByCode.getGameStatus() != GameStatus.WAITING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, joinErrorMessage);
        }

        String userErrorMessage = "You cannot join your own game.";
        if (gameByCode.getHost().getUserId().equals(guestId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, userErrorMessage);
        }

        gameByCode.setGuest(guest);
        gameByCode.setGameStatus(GameStatus.GUEST_SET);

        // save to repo and flush
        gameByCode = gameRepository.save(gameByCode);
        gameRepository.flush();

        return gameByCode;
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
        List<Game> waitingGames = this.gameRepository.findByGameStatusAndIsPrivate(GameStatus.WAITING, false);
        Game nextGame = waitingGames.isEmpty() ? null : waitingGames.get(0);

        // throw errror if no waiting games
        String gameErrorMessage = "There is no open game to join. Try creating your own game.";
        if (nextGame == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format(gameErrorMessage));
        }

        // make sure the user is not joining their own game, loop through list
        Game validGame = null;
        for (Game game : waitingGames) {
            // if the host in the game is not the user trying to join, set it as the first
            // valid game and exit the loop
            if (!game.getHost().getUserId().equals(guestId)) {
                validGame = game;
                break;
            }
        }

        // throw errror if no valid games
        gameErrorMessage = "The only open games to join were created by you. You cannot join your own game. Please wait for another player to join you.";
        if (validGame == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format(gameErrorMessage));
        }

        // add guest to game
        validGame.setGuest(guest);
        validGame.setGameStatus(GameStatus.GUEST_SET);

        // save to repo and flush
        validGame = gameRepository.save(validGame);
        gameRepository.flush();

        return validGame;

    }

    public Game websocketJoin(Long gameId, Long playerId, String sessionId) throws IOException, InterruptedException {
        // get the correct game
        Game game = getGame(gameId);
        if (game == null) {
            websocketService.sendInvalidGameMsg(playerId, gameId);
            return game;
        }

        // if the game is already closed, send the game info back without joining it
        if (game.getGameStatus().equals(GameStatus.DISCONNECTED) || game.getGameStatus()
                .equals(GameStatus.FINISHED) || game.getGameStatus().equals(GameStatus.SURRENDERED)) {
            websocketService.sendToLobby(gameId, WebSockDTOMapper.INSTANCE.convertEntityToWSGameStatusDTO(game));
            return game;
        }

        // update the host/guest status in the game
        if (playerId.equals(game.getHost().getUserId())) {
            game.setHostStatus(PlayerStatus.CONNECTED);

            // check if this is a reconnect with a different session
            if (game.getHostSessionId() != null) {
                String oldSessionId = game.getHostSessionId();
                websocketService.cancelDisconnect(playerId, oldSessionId);
            }
            game.setHostSessionId(sessionId);

        } else if (playerId.equals(game.getGuest().getUserId())) {
            game.setGuestStatus(PlayerStatus.CONNECTED);
            // check if this is a reconnect with a different session
            if (game.getGuestSessionId() != null) {
                String oldSessionId = game.getGuestSessionId();
                websocketService.cancelDisconnect(playerId, oldSessionId);
            }
            game.setGuestSessionId(sessionId);
        }

        // update the game status and send updates to users on home page
        if (game.getHostStatus().equals(PlayerStatus.CONNECTED)
                && game.getGuestStatus().equals(PlayerStatus.CONNECTED)) {
            game.setGameStatus(GameStatus.CONNECTED);

        } else if ((game.getHostStatus().equals(
                PlayerStatus.CONNECTED)
                || game.getGuestStatus().equals(PlayerStatus.CONNECTED)
                        && !game.getGameStatus().equals(GameStatus.DISCONNECTED))) {
            game.setGameStatus(GameStatus.WAITING);

        }

        // save to repo and flush
        game = gameRepository.save(game);
        gameRepository.flush();
        sendPublicGamesUpdate();

        // send the game update as dto via the lobby channel
        websocketService.sendToLobby(gameId, WebSockDTOMapper.INSTANCE.convertEntityToWSGameStatusDTO(game));

        return game;
    }

    public Game startGame(Long gameId, Long playerId) throws IOException, InterruptedException {

        // update the game status
        Game game = getGame(gameId);

        if (game == null) {
            websocketService.sendInvalidGameMsg(playerId, gameId);
            return game;
        }

        // if the guest is trying to start the game, just return the game as is
        if (game.getGuest().getUserId().equals(playerId) || game.getGameStatus().equals(GameStatus.ONGOING)) {
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
        websocketService.sendToGame(gameId, WebSockDTOMapper.INSTANCE.convertEntityToWSGameStatusDTO(game));
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
            websocketService.sendInvalidMoveMsg(message.getPlayerId(), gameId);
            return game;
        }

        // execute the move and update the round
        Round updatedRound = roundService.executeMove(game.getCurrentRound(), message);
        game.setCurrentRound(updatedRound);

        // do post move checks and determine if the round was finished
        Boolean endOfRound = roundService.postMoveChecks(updatedRound);

        // send the updated round to the players
        websocketService.sendRoundUpdate(game, game.getCurrentRound());

        // if the round was finished, updates the points and check if there is a winner
        if (endOfRound) {
            checkWinner(updatePoints(game));
        }

        // update the game in the repository and return it
        game = gameRepository.save(game);
        gameRepository.flush();
        return game;
    }

    public Game updatePoints(Game game) {
        // add points from the finished round (still set as current) to the total points
        // on the game
        game.setGuestPoints(game.getGuestPoints() + game.getCurrentRound().getGuestPoints());
        game.setHostPoints(game.getHostPoints() + game.getCurrentRound().getHostPoints());
        return game;
    }

    public void checkWinner(Game game) throws IOException, InterruptedException {
        // check if there is a winner, i.e. if one of the players has at least 11 points
        // and 2 more than the other player
        if (((game.getGuestPoints() >= 11 || game.getHostPoints() >= 11)
                && java.lang.Math.abs(game.getGuestPoints() - game.getHostPoints()) >= 2) || game.getIsSingleRound()) {

            // check if it is the guest or the host who won and set them as the winnre
            if (game.getGuestPoints() > game.getHostPoints()) {
                game.setGuest(userService.updateUserWinStatistics(game.getGuest(), true));
                game.setHost(userService.updateUserWinStatistics(game.getHost(), false));
                game.setWinner(game.getGuest());
            } else {
                userService.updateUserWinStatistics(game.getGuest(), false);
                game.setGuest(userService.updateUserWinStatistics(game.getGuest(), false));
                game.setHost(userService.updateUserWinStatistics(game.getHost(), true));
                game.setWinner(game.getHost());
            }
            // update the game status
            game.setGameStatus(GameStatus.FINISHED);
        }

        game = gameRepository.save(game);
        // send the game update as dto via the Game channel
        websocketService.sendToGame(game.getGameId(),
                WebSockDTOMapper.INSTANCE.convertEntityToWSGameStatusDTO(game));
    }

    public void reconnect(Long gameId, Long playerId, String sessionId) {
        // find the game
        Game game = getGame(gameId);
        if (game == null) {
            websocketService.sendInvalidGameMsg(playerId, gameId);
            return;
        }
        String oldSessionId = null;

        // figure out which player is reconnecting and get their old session id, update
        // to new id
        if (playerId.equals(game.getHost().getUserId())) {
            oldSessionId = game.getHostSessionId();
            game.setHostSessionId(sessionId);
        } else if (playerId.equals(game.getGuest().getUserId())) {
            oldSessionId = game.getGuestSessionId();
            game.setGuestSessionId(sessionId);
        } else {
            websocketService.sendInvalidUserMsg(playerId, gameId);
        }

        // cancel disconnecting for the previous session disconnect event
        websocketService.cancelDisconnect(playerId, oldSessionId);

        // save the game with the updated session
        gameRepository.save(game);

        // send the current game status to the reconnecting user
        websocketService.sendGameToUser(playerId, game.getGameId(),
                WebSockDTOMapper.INSTANCE.convertEntityToWSGameStatusDTO(game));

        // send the current round info to the reconnecting user
        websocketService.sendRoundInfoToUser(game, game.getCurrentRound(), playerId);
    }

    public void findDisconnectedPlayer(String sessionId) {
        Game game = null;
        Role role = null;
        Long userId = null;

        // find the player that has disconnected
        Game guestGame = gameRepository.findByGuestSessionId(sessionId);
        Game hostGame = gameRepository.findByHostSessionId(sessionId);

        // abort if no game was found at all
        if ((guestGame == null) && (hostGame == null)) {
            return;
        }

        if (guestGame != null) {
            game = guestGame;
            role = Role.GUEST;
            userId = guestGame.getGuest().getUserId();

        } else {
            game = hostGame;
            role = Role.HOST;
            userId = hostGame.getHost().getUserId();
        }

        // quickly check that the game is still active
        if (game.getGameStatus().equals(GameStatus.DISCONNECTED) || game.getGameStatus()
                .equals(GameStatus.FINISHED) || game.getGameStatus().equals(GameStatus.SURRENDERED)) {
            return;
        }

        websocketService.startReconnectTimer(5, game, role, userId, sessionId);
    }

    public void surrender(Long gameId, Long playerId) {
        // find the game & player username
        Game game = getGame(gameId);
        if (game == null) {
            websocketService.sendInvalidGameMsg(playerId, gameId);
            return;
        }

        // id matches host => host surrendered
        if (playerId.equals(game.getHost().getUserId())) {
            game.setEndGameReason("Player " + game.getHost().getUsername() + " surrendered the game.");
            game.setGuest(userService.updateUserWinStatistics(game.getGuest(), true));
            game.setHost(userService.updateUserWinStatistics(game.getHost(), false));
            game.setWinner(game.getGuest());
            // id matches guest => guest surrendered
        } else if (playerId.equals(game.getGuest().getUserId())) {
            game.setEndGameReason("Player " + game.getGuest().getUsername() + " surrendered the game.");
            game.setGuest(userService.updateUserWinStatistics(game.getGuest(), false));
            game.setHost(userService.updateUserWinStatistics(game.getHost(), true));
            game.setWinner(game.getHost());
        } else {
            return;
        }

        // update the status
        game.setGameStatus(GameStatus.SURRENDERED);

        // save and share the update
        gameRepository.save(game);
        websocketService.sendToGame(gameId, WebSockDTOMapper.INSTANCE.convertEntityToWSGameStatusDTO(game));
    }

    public void confirmEOR(Long gameId, Long playerId) throws IOException, InterruptedException {

        Game game = getGame(gameId);
        if (game == null) {
            websocketService.sendInvalidGameMsg(playerId, gameId);
            return;
        }

        // skip if the game has finished
        if (game.getGameStatus().equals(GameStatus.FINISHED)) {
            return;
        }

        // update the host/guest confirmed End of Round (EOR) status in the game
        if (playerId.equals(game.getHost().getUserId())) {
            game.getCurrentRound().setHostConfirmedEOR(true);
        } else if (playerId.equals(game.getGuest().getUserId())) {
            game.getCurrentRound().setGuestConfirmedEOR(true);
        }

        if (game.getCurrentRound().getHostConfirmedEOR() && game.getCurrentRound().getGuestConfirmedEOR()) {
            // create a new round for the game
            game.setCurrentRound(roundService.newRound(game));
            game.setTotalRounds(game.getTotalRounds() + 1);

            // send the updated round to the players
            websocketService.sendRoundUpdate(game, game.getCurrentRound());

            // save the game
            gameRepository.save(game);
        }

    }

}