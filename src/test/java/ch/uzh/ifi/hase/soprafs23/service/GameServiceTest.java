package ch.uzh.ifi.hase.soprafs23.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

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
        Mockito.when(gameRepository.findByGameId(3L)).thenReturn(testGame);
    }

    // test that the host joining the game updates statuses as expected
    @Test
    public void websocketjoin_validInputs_host_success() {
        // websocket join test host to test game
        Game updatedGame = gameService.websocketJoin(testGame.getGameId(), testHost.getUserId());
        
        // check statuses
        assertEquals(PlayerStatus.CONNECTED, updatedGame.getHostStatus());
        assertEquals(GameStatus.WAITING, updatedGame.getGameStatus());
    }

    // test that the guest joining the game updates statuses as expected
    @Test
    public void websocketjoin_validInputs_guest_success() {
        // websocket join test guest to test game
        Game updatedGame = gameService.websocketJoin(testGame.getGameId(), testGuest.getUserId());

        // check statuses
        assertEquals(PlayerStatus.CONNECTED, updatedGame.getGuestStatus());
        assertEquals(GameStatus.WAITING, updatedGame.getGameStatus());
    }

    // test that both the host and the guest joining the game updates statuses as expected
    @Test
    public void websocketjoin_validInputs_guest_host_success() {

        // websocket join test host and guest to test game
        Game updatedGame = gameService.websocketJoin(testGame.getGameId(), testHost.getUserId());updatedGame = gameService.websocketJoin(updatedGame.getGameId(), testGuest.getUserId());
        
        // check statuses
        assertEquals(PlayerStatus.CONNECTED, updatedGame.getHostStatus());
        assertEquals(PlayerStatus.CONNECTED, updatedGame.getGuestStatus());
        assertEquals(GameStatus.CONNECTED, updatedGame.getGameStatus());
    }
}
