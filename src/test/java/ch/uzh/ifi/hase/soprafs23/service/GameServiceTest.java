package ch.uzh.ifi.hase.soprafs23.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import static org.mockito.BDDMockito.given;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerStatus;
import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerMoveMessage;
import ch.uzh.ifi.hase.soprafs23.entity.Round;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.CardDeckRepository;
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

    @Mock
    private CardDeckRepository cardDeckRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private GameService gameService;

    @Mock
    private RoundService roundService;

    @Mock
    private CardDeckService cardDeckService;

    @Mock
    private MoveLogicService moveLogicService;

    private User testHost;
    private User testGuest;
    private Game testGame;
    private Round testRound;
    private PlayerMoveMessage mockPlayerMoveMessage;

    @BeforeEach
    public void setup() {
        // initial setup so that test host, guest, and game are available to work with
        // from the repositories

        MockitoAnnotations.openMocks(this);

        testRound = new Round();

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
        testGame.setCurrentRound(testRound);

        mockPlayerMoveMessage = new PlayerMoveMessage();

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testHost);

        Mockito.when(userRepository.findByUserId(1L)).thenReturn(testHost);
        Mockito.when(userRepository.findByUserId(2L)).thenReturn(testGuest);
        Mockito.when(gameRepository.findByGameId(3L)).thenReturn(testGame);

        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);

    }

    @Test
    public void makeMove_success() {
        Mockito.when(gameService.getGame(Mockito.any())).thenReturn(testGame);
        Mockito.when(moveLogicService.checkMove(mockPlayerMoveMessage)).thenReturn(true);
        Mockito.when(roundService.executeMove(Mockito.any(), Mockito.any())).thenReturn(testRound);
        testGame = gameService.makeMove(3L, mockPlayerMoveMessage);

        assertEquals(testRound, testGame.getCurrentRound());
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

        // assert that the guest has been added to the game and the game status is
        // correct
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
        testGame.setGuestStatus(PlayerStatus.WAITING);
        // websocket join test host to test game
        Game updatedGame = gameService.websocketJoin(testGame.getGameId(), testHost.getUserId());

        // check statuses
        assertEquals(PlayerStatus.CONNECTED, updatedGame.getHostStatus());
        assertEquals(GameStatus.WAITING, updatedGame.getGameStatus());
    }

    // test that the guest joining the game updates statuses as expected
    @Test
    public void websocketjoin_validInputs_guest_success() throws IOException, InterruptedException {
        testGame.setHostStatus(PlayerStatus.WAITING);
        // websocket join test guest to test game
        Game updatedGame = gameService.websocketJoin(testGame.getGameId(), testGuest.getUserId());

        // check statuses
        assertEquals(PlayerStatus.CONNECTED, updatedGame.getGuestStatus());
        assertEquals(GameStatus.WAITING, updatedGame.getGameStatus());
    }

    // test that both the host and the guest joining the game updates statuses as
    // expected
    @Test
    public void websocketjoin_validInputs_guest_host_success() throws IOException, InterruptedException {
        testGame.setHostStatus(PlayerStatus.WAITING);
        testGame.setGuestStatus(PlayerStatus.WAITING);

        // websocket join test host and guest to test game
        Game updatedGame = gameService.websocketJoin(testGame.getGameId(), testHost.getUserId());
        updatedGame = gameService.websocketJoin(updatedGame.getGameId(), testGuest.getUserId());

        // check statuses
        assertEquals(PlayerStatus.CONNECTED, updatedGame.getHostStatus());
        assertEquals(PlayerStatus.CONNECTED, updatedGame.getGuestStatus());
        assertEquals(GameStatus.CONNECTED, updatedGame.getGameStatus());
    }

    @Test
    public void startGame_success() throws IOException, InterruptedException {
        Round testRound = new Round();
        testRound.setRoundId(4L);

        given(roundService.newRound(Mockito.any())).willReturn(testRound);

        // // start the game
        Game updatedGame = gameService.startGame(testGame.getGameId());

        // // check status
        assertEquals(GameStatus.ONGOING, updatedGame.getGameStatus());
    }

    // update with mock random or something
    @Test
    public void setStartingPlayer_success() {

    }
}
