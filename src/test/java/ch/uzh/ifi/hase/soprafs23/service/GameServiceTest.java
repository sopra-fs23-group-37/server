package ch.uzh.ifi.hase.soprafs23.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private GameService gameService;


    private User testHost;
    private User testGuest;
    private Game testGame;

    @BeforeEach
    public void setup() {
        // initial setup so that test host, guest, and game are available to work with from the repositories
        MockitoAnnotations.openMocks(this);

        testHost = new User();
        testHost.setUsername("testUsername");
        testHost.setUserId(1L);

        testGuest = new User();
        testGuest.setUsername("testUsername2");
        testGuest.setUserId(2L);

        testGame = new Game();
        testGame.setHost(testHost);
        testGame.setGuest(testGuest);
        testGame.setGameId(3L);


        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testHost);
        
        Mockito.when(userRepository.findByUserId(1L)).thenReturn(testHost);
        Mockito.when(userRepository.findByUserId(2L)).thenReturn(testGuest);
        Mockito.when(gameRepository.findByGameId(3L)).thenReturn(testGame);

        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
    }


    // test that a valid guest joining the game updates the game as expected
    @Test
    public void joinGame_validInputs_success() {
        
        // make sure the game is in the right status
        testGame.setGameStatus(GameStatus.WAITING);
        List<Game> waitingGames = new ArrayList<>();
        waitingGames.add(testGame);
        Mockito.when(gameRepository.findByGameStatus(GameStatus.WAITING)).thenReturn(waitingGames);

        // join the game with valid guest id
        Game updatedGame = gameService.joinGame(testGuest.getUserId());

        // assert that the guest has been added to the game and the game status is correct
        assertEquals(testGuest, updatedGame.getGuest());
        assertEquals(GameStatus.GUEST_SET, updatedGame.getGameStatus());
    }


    // test exceptions - no games
    @Test
    public void joinGame_noGames_throwsException() {
        
        // simulate no games returned from the Repo
        List<Game> waitingGames = new ArrayList<>();
        Mockito.when(gameRepository.findByGameStatus(GameStatus.WAITING)).thenReturn(waitingGames);

        // assert exception
        assertThrows(ResponseStatusException.class, () -> gameService.joinGame(testGuest.getUserId()));
    }
    
    // test exceptions - invalid user id
    @Test
    public void joinGame_invalidGuest_throwsException() {
        
        // make sure the game is in the right status
        testGame.setGameStatus(GameStatus.WAITING);
        List<Game> waitingGames = new ArrayList<>();
        waitingGames.add(testGame);
        Mockito.when(gameRepository.findByGameStatus(GameStatus.WAITING)).thenReturn(waitingGames);

        Long invalidId = 9L;

        // assert exception
        assertThrows(ResponseStatusException.class, () -> gameService.joinGame(invalidId));
    }



    // test that the host joining the game updates statuses as expected
    @Test
    public void websocketjoin_validInputs_host_success() throws IOException, InterruptedException {
        // websocket join test host to test game
        

        Game updatedGame = gameService.websocketJoin(testGame.getGameId(), testHost.getUserId());

        
        
        // check statuses
        assertEquals(PlayerStatus.CONNECTED, updatedGame.getHostStatus());
        assertEquals(GameStatus.WAITING, updatedGame.getGameStatus());
    }

    // test that the guest joining the game updates statuses as expected
    @Test
    public void websocketjoin_validInputs_guest_success() throws IOException, InterruptedException {
        // websocket join test guest to test game
        Game updatedGame = gameService.websocketJoin(testGame.getGameId(), testGuest.getUserId());

        // check statuses
        assertEquals(PlayerStatus.CONNECTED, updatedGame.getGuestStatus());
        assertEquals(GameStatus.WAITING, updatedGame.getGameStatus());
    }

    // test that both the host and the guest joining the game updates statuses as expected
    @Test
    public void websocketjoin_validInputs_guest_host_success() throws IOException, InterruptedException {

        // websocket join test host and guest to test game
        Game updatedGame = gameService.websocketJoin(testGame.getGameId(), testHost.getUserId());updatedGame = gameService.websocketJoin(updatedGame.getGameId(), testGuest.getUserId());
        
        // check statuses
        assertEquals(PlayerStatus.CONNECTED, updatedGame.getHostStatus());
        assertEquals(PlayerStatus.CONNECTED, updatedGame.getGuestStatus());
        assertEquals(GameStatus.CONNECTED, updatedGame.getGameStatus());
    }
}
