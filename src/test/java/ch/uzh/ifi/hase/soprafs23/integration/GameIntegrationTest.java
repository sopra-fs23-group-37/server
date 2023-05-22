package ch.uzh.ifi.hase.soprafs23.integration;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerStatus;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerJoinMessage;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.WSGameStatusDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.WSHomeDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.WSStatsDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "server.port=8080",
        "spring.h2.console.enabled=true",
        "spring.h2.console.settings.web-allow-others=true",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect"
})
public class GameIntegrationTest {

    private Game testGame;
    private User hostUser;
    private User guestUser;

    @LocalServerPort
    private Integer port;
    private WebSocketStompClient webSocketStompClient;
    private StompSession session;
    private CompletableFuture<WSHomeDTO> completableFutureHome;
    private CompletableFuture<WSGameStatusDTO> completableFutureLobby;
    private CompletableFuture<WSStatsDTO> completableFutureGuestStats;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    public void setup() throws InterruptedException, ExecutionException, TimeoutException {

        userRepository.deleteAll();
        gameRepository.deleteAll();

        // set up basic data for users and games
        hostUser = new User();
        guestUser = new User();
        testGame = new Game();

        // hostUser.setUserId(1L);
        hostUser.setUsername("testHost");
        hostUser.setPassword("password");
        hostUser.setCreation_date(new Date());
        hostUser.setToken(UUID.randomUUID().toString());
        hostUser.setUserStatus(UserStatus.ONLINE);
        // guestUser.setUserId(2L);
        guestUser.setUsername("testGuest");
        guestUser.setPassword("password");
        guestUser.setCreation_date(new Date());
        guestUser.setToken(UUID.randomUUID().toString());
        guestUser.setUserStatus(UserStatus.ONLINE);
        guestUser.setGamesPlayed(4L);
        guestUser.setGamesWon(2L);

        hostUser = userRepository.saveAndFlush(hostUser);
        guestUser = userRepository.saveAndFlush(guestUser);

        testGame.setHost(hostUser);
        testGame.setGuest(guestUser);
        testGame.setGameStatus(GameStatus.WAITING);
        testGame.setHostStatus(PlayerStatus.CONNECTED);

        System.out.println(testGame);

        testGame = gameRepository.saveAndFlush(testGame);

        // set up websocket connection
        setWsConnection();

        // add subscription for Home Screen
        completableFutureHome = new CompletableFuture<>();
        session.subscribe("/topic/game/home", new StompFrameHandler() {

            @Override
            public java.lang.reflect.Type getPayloadType(StompHeaders headers) {
                return WSHomeDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object o) {
                completableFutureHome.complete((WSHomeDTO) o);
            }
        });

        // add subscription for Lobby
        completableFutureLobby = new CompletableFuture<>();
        session.subscribe("/topic/game/" + testGame.getGameId() + "/lobby", new StompFrameHandler() {

            @Override
            public java.lang.reflect.Type getPayloadType(StompHeaders headers) {
                return WSGameStatusDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object o) {
                completableFutureLobby.complete((WSGameStatusDTO) o);
            }
        });

        // add subscription for Stats for guest
        completableFutureGuestStats = new CompletableFuture<>();
        session.subscribe("/queue/user/" + guestUser.getUserId() + "/statistics", new StompFrameHandler() {

            @Override
            public java.lang.reflect.Type getPayloadType(StompHeaders headers) {
                return WSStatsDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object o) {
                completableFutureGuestStats.complete((WSStatsDTO) o);
            }
        });

    }

    // test websocket home
    @Test
    void sendHome() throws Exception {

        // create a player join message for the guest user
        PlayerJoinMessage playerJoinMessage = new PlayerJoinMessage(guestUser.getUserId());

        // send the player join message to the server over STOMP
        session.send("/game/home", playerJoinMessage);

        // wait for the DTOs to be received over STOMP
        WSStatsDTO statsReceived = completableFutureGuestStats.get(20, TimeUnit.SECONDS);
        WSHomeDTO homeReceived = completableFutureHome.get(20, TimeUnit.SECONDS);

        // assert that the received DTOs are not null and match the expected DTOs
        // Home DTO
        assertNotNull(homeReceived);
        assertEquals(1, homeReceived.getNumberOpenGames());

        // Stats DTO
        assertNotNull(statsReceived);
        assertEquals(4, statsReceived.getGamesPlayed());
        assertEquals(2, statsReceived.getGamesWon());

    }

    // test websocket join
    @Test
    void sendGuestJoinMessageReturnsSuccess() throws Exception {

        // create a player join message for the guest user
        PlayerJoinMessage playerJoinMessage = new PlayerJoinMessage(guestUser.getUserId());

        // send the player join message to the server over STOMP
        session.send("/game/join/" + testGame.getGameId(), playerJoinMessage);

        // wait for the DTOs to be received over STOMP
        WSHomeDTO receivedHomeDTO = completableFutureHome.get(20, TimeUnit.SECONDS);
        WSGameStatusDTO receivedLobbyDTO = completableFutureLobby.get(20, TimeUnit.SECONDS);

        // assert that the received DTOs are not null and match the expected DTOs
        // Home DTO
        assertNotNull(receivedHomeDTO);
        assertEquals(0, receivedHomeDTO.getNumberOpenGames());

        // Lobby DTO
        assertNotNull(receivedLobbyDTO);
        assertEquals(GameStatus.CONNECTED, receivedLobbyDTO.getGameStatus());
        assertEquals(PlayerStatus.CONNECTED, receivedLobbyDTO.getGuestStatus());

    }

    // helper method to set websocket connection, not overlading setup method
    private void setWsConnection() throws InterruptedException, ExecutionException, TimeoutException {
        webSocketStompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {
                }).get(20, TimeUnit.SECONDS);

    }

}
