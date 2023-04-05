package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerStatus;
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

    @Autowired
    public GameService(@Qualifier("userRepository") UserRepository userRepository, @Qualifier("gameRepository") GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
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

        // save to repo and flush
        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        log.debug("Created Information for Session: {}", newGame);
        return newGame;
    }

    public Game getGame(Long gameId) {
        return this.gameRepository.findByGameId(gameId);
    }

    public Game websocketJoin(Long gameId, Long playerId) {
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
        } else if(game.getHostStatus() == PlayerStatus.CONNECTED || game.getGuestStatus() == PlayerStatus.CONNECTED) {
            game.setGameStatus(GameStatus.WAITING);	
        }

        // return the updated game
        return game;

        }
        
        

        



}