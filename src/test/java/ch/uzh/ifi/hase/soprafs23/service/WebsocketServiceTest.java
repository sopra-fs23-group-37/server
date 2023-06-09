package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerStatus;
import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.constant.WSErrorType;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.CardDeck;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Round;
import ch.uzh.ifi.hase.soprafs23.entity.User;

import ch.uzh.ifi.hase.soprafs23.rest.dto.WSErrorMessageDTO;

import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.WSHomeDTO;

import ch.uzh.ifi.hase.soprafs23.rest.dto.WSRoundStatusDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.WSStatsDTO;

class WebsocketServiceTest {

    @Mock
    private SimpMessagingTemplate simp;

    @Mock
    private GameService gameService;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private WebsocketService websocketService;

    private Long testUserId;

    private Long testGameId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUserId = 9L;
        testGameId = 10L;

    }

    @Test
    void testSendGamesUpdateToHome() {
        List<Game> openGames = new ArrayList<>();
        Game game1 = new Game();
        Game game2 = new Game();
        openGames.add(game1);
        openGames.add(game2);
        when(gameRepository.findByGameStatusAndIsPrivate(GameStatus.WAITING, false)).thenReturn(openGames);

        WSHomeDTO dto = websocketService.sendGamesUpdateToHome();
        String destination = "/topic/game/home";

        verify(simp).convertAndSend(destination, dto);
        assertEquals(2, dto.getNumberOpenGames());
    }

    @Test
    void testSendStatsToUser() {
        // given this user played and won this many games
        User user = new User();
        user.setUserId(testUserId);
        user.setGamesPlayed(5L);
        user.setGamesWon(3L);

        // create the dto
        WSStatsDTO dto = websocketService.sendStatsUpdateToUser(user);
        String destination = "/queue/user/" + testUserId + "/statistics";

        // verify the send and content
        verify(simp).convertAndSend(destination, dto);
        assertEquals(5, dto.getGamesPlayed());
        assertEquals(3, dto.getGamesWon());
    }

    @Test
    void testSendToLobby() {
        Long gameId = 1L;
        Object dto = new Object();
        String destination = "/topic/game/1/lobby";
        websocketService.sendToLobby(gameId, dto);
        verify(simp).convertAndSend(destination, dto);
    }

    @Test
    void testSendToGame() {
        Long gameId = 1L;
        Object dto = new Object();
        String destination = "/topic/game/1/game";
        websocketService.sendToGame(gameId, dto);
        verify(simp).convertAndSend(destination, dto);
    }

    @Test
    void testSendToRound() {
        Long gameId = 1L;
        Object dto = new Object();
        String destination = "/topic/game/1/round";
        websocketService.sendToRound(gameId, dto);
        verify(simp).convertAndSend(destination, dto);
    }

    @Test
    void testCreateWSRoundStatusDTOforUser() {
        Game game = new Game();
        Round round = new Round();
        User hostUser = new User();
        User guestUser = new User();
        Player host = new Player();
        Player guest = new Player();
        Card card = new Card();
        CardDeck cardDeck = new CardDeck();
        List<Card> cards = new ArrayList<>();

        cards.add(card);
        cardDeck.setRemaining(4);
        round.setCardDeck(cardDeck);
        hostUser.setUserId(1L);
        guestUser.setUserId(2L);
        host.addCardsToHand(cards);
        game.setHost(hostUser);
        game.setGuest(guestUser);
        guest.addCardToDiscard(card);
        round.setHost(host);
        round.setGuest(guest);
        round.setCurrentTurnPlayer(Role.GUEST);
        round.addCardsToTable(cards);
        WSRoundStatusDTO dto = websocketService.createWSRoundStatusDTOforUser(game, round, 2L);
        assert (dto.getMyTurn() == true);

    }

    @Test
    public void sendErrorToUserTest() {
        Object dto = new Object();
        String destination = "/queue/user/" + testUserId + "/" + testGameId + "/error";
        websocketService.sendErrorToUser(testUserId, testGameId, dto);
        verify(simp).convertAndSend(destination, dto);
    }

    @Test
    public void createInvalidMoveMessageTest() {
        WSErrorMessageDTO dto = new WSErrorMessageDTO();
        dto.setType(WSErrorType.INVALIDMOVE);
        dto.setMessage("This is not a valid move. Please try a different move");

        WSErrorMessageDTO createdDto = websocketService.createInvalidMoveMsg();

        assertEquals(dto.getType(), createdDto.getType());
        assertEquals(dto.getMessage(), createdDto.getMessage());
    }

    @Test
    public void createInvalidGameMessageTest() {
        WSErrorMessageDTO dto = new WSErrorMessageDTO();
        dto.setType(WSErrorType.INVALIDGAME);
        dto.setMessage("There is no Game under this link. Please create or join a game from the Home page");

        WSErrorMessageDTO createdDto = websocketService.createInvalidGameMsg();

        assertEquals(dto.getType(), createdDto.getType());
        assertEquals(dto.getMessage(), createdDto.getMessage());
    }

    @Test
    public void createInvalidUserMessageTest() {
        WSErrorMessageDTO dto = new WSErrorMessageDTO();
        dto.setType(WSErrorType.INVALIDUSER);
        dto.setMessage(
                "You tried to play a game you are not a part of! Please create or join a game from the Home screen.");

        WSErrorMessageDTO createdDto = websocketService.createInvalidUserMsg();

        assertEquals(dto.getType(), createdDto.getType());
        assertEquals(dto.getMessage(), createdDto.getMessage());
    }

    @Test
    void testDisconnectPlayerHost() {
        Game game = new Game();
        User host = new User();
        host.setUsername("hostname");
        game.setHost(host);
        game.setHostStatus(PlayerStatus.CONNECTED);
        game.setGameStatus(GameStatus.ONGOING);

        websocketService.disconnectPlayer(game, Role.HOST);

        assertEquals(GameStatus.DISCONNECTED, game.getGameStatus());
        assertEquals(PlayerStatus.DISCONNECTED, game.getHostStatus());
        assertEquals("Player hostname unexpectedly disconnected from the game.", game.getEndGameReason());

    }

}
