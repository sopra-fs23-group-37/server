package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerStatus;
import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerJoinMessage;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerMoveMessage;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.CardDeckRepository;
import ch.uzh.ifi.hase.soprafs23.repository.CardRepository;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoundRepository;
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

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final RoundRepository roundRepository;
    private final PlayerRepository playerRepository;
    private final CardDeckRepository cardDeckRepository;
    private final CardRepository cardRepository;
    private final MoveLogicService moveLogicService = new MoveLogicService();



    private final RoundService roundService;
       //private final UserService userService;


    @Autowired
    public GameService(@Qualifier("userRepository") UserRepository userRepository, @Qualifier("gameRepository") GameRepository gameRepository, @Qualifier("roundRepository") RoundRepository roundRepository, @Qualifier("playerRepository") PlayerRepository playerRepository, @Qualifier("cardDeckRepository") CardDeckRepository cardDeckRepository, @Qualifier("cardRepository") CardRepository cardRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.roundRepository = roundRepository;
        this.playerRepository = playerRepository;
        this.cardDeckRepository = cardDeckRepository;
        this.cardRepository = cardRepository;
        this.roundService = new RoundService(roundRepository, playerRepository, cardDeckRepository, cardRepository, gameRepository);
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
        if(host == null) {
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage,hostId));
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
        if(guest == null) {
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, 
            String.format(playerErrorMessage, guestId));
        }

        // get open games and pick oldest one
        List<Game> waitingGames = this.gameRepository.findByGameStatus(GameStatus.WAITING);
        Game nextGame = waitingGames.isEmpty() ? null : waitingGames.get(0);

        // throw errror if no waiting games
        String gameErrorMessage = "There is no open game to join. Try creating your own game.";
        if (nextGame == null) {
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, 
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
        if(playerId == game.getHost().getUserId()) {
            game.setHostStatus(PlayerStatus.CONNECTED);
        } else if(playerId == game.getGuest().getUserId()) {
            game.setGuestStatus(PlayerStatus.CONNECTED);
        } 

        // update the game status
        if(game.getHostStatus() == PlayerStatus.CONNECTED && game.getGuestStatus() == PlayerStatus.CONNECTED) {
            game.setGameStatus(GameStatus.CONNECTED);
            //startGame(game);
        } else if(game.getHostStatus() == PlayerStatus.CONNECTED || game.getGuestStatus() == PlayerStatus.CONNECTED) {
            game.setGameStatus(GameStatus.WAITING);	
        }

        // save to repo and flush
        game = gameRepository.save(game);
        gameRepository.flush();

        // return the updated game
        return game;
        }
        
    public Game startGame(Long gameId) throws IOException, InterruptedException {
        
        // update the game status
        Game game = getGame(gameId);
        game = setStartingPlayer(game);
        game.setGameStatus(GameStatus.ONGOING);

        // save to repo and flush
        game = gameRepository.save(game);
        gameRepository.flush();

        // TODO: start the first round, i.e. fix this method...
        game = roundService.newRound(gameId);

        return game;
    }    

    public Game setStartingPlayer(Game game) {

        // TODO: write a proper method to set the starting player
        Random rnd = new Random();

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

    public Game makeMove(long gameId, PlayerMoveMessage message) {
        Game game = getGame(gameId);
        Game updatedGame = moveLogicService.checkMove(game, message);
        
        return game;
    }



}