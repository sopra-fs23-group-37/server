package ch.uzh.ifi.hase.soprafs23.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;


import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerJoinMessage;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.service.GameService;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WSGameControllerTest {

  @MockBean
  private GameService gameService;
  
    @LocalServerPort
    private Integer port;
    private WebSocketStompClient webSocketStompClient;
    private StompSession session;
    private Game game;
    private User host;
    private User guest;
    private CompletableFuture<Game> completableFuture;

    @BeforeEach
    public void setup() throws Exception {
    
    completableFuture = new CompletableFuture<>();

    host = new User();
    guest = new User();
    game = new Game();

    host.setUserId(1L);
    guest.setUserId(2L);
    game.setGameId(3L);
    game.setHost(host);
    game.setGuest(guest);

    given(gameService.websocketJoin(Mockito.any(), Mockito.any())).willReturn(game);

    webSocketStompClient = new WebSocketStompClient(new SockJsClient(
    List.of(new WebSocketTransport(new StandardWebSocketClient()))));

    webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

    session = webSocketStompClient
        .connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {}).get(20, TimeUnit.SECONDS); 

    session.subscribe("/topic/game/3", new StompFrameHandler() {
 
    @Override
    public java.lang.reflect.Type getPayloadType(StompHeaders headers) {
      return Game.class;
    }
 
    @Override
    public void handleFrame(StompHeaders headers, Object o) {
      completableFuture.complete((Game) o);
    }
  });

    }

    @Test
    void verifyGameIsReceived() throws Exception {

        // mock the game service to return the game when a user joins
        given(gameService.websocketJoin(Mockito.any(), Mockito.any())).willReturn(game);

        // create a WebSocket message converter to convert the player join message to a message that can be sent over STOMP
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

        // create a player join message for the host user
        PlayerJoinMessage playerJoinMessage = new PlayerJoinMessage(1L);

        // send the player join message to the server over STOMP
        session.send("/game/join/3", converter.toMessage(playerJoinMessage, null));

        // wait for the game to be received over STOMP
        Game receivedGame = completableFuture.get(60, TimeUnit.SECONDS);

        // assert that the received game is not null and matches the expected game
        assertNotNull(receivedGame);
        assertEquals(game.getGameId(), receivedGame.getGameId());
        assertEquals(game.getHost().getUserId(), receivedGame.getHost().getUserId());
        assertEquals(game.getGuest().getUserId(), receivedGame.getGuest().getUserId());

        // verify that the game service was called once with the correct arguments
        verify(gameService, times(1)).websocketJoin(eq(3L), eq(null));
    }


@Test
void verifyStartGameIsReceived() throws Exception {

  given(gameService.startGame(Mockito.any())).willReturn(game);

  session.send("/game/start/3",null);

  Game receivedGame = completableFuture.get(60, TimeUnit.SECONDS);

  assertNotNull(receivedGame);
}

}
