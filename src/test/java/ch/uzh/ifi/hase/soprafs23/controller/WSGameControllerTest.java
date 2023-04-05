package ch.uzh.ifi.hase.soprafs23.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import static org.mockito.BDDMockito.given;

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

    }

@Test
void verifyGameIsReceived() throws Exception {

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
 
  given(gameService.websocketJoin(Mockito.any(), Mockito.any())).willReturn(game);

  MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

  PlayerJoinMessage playerJoinMessage = new PlayerJoinMessage(1L);

  session.send("/game/join/3", converter.toMessage(playerJoinMessage, null));

  Game receivedGame = completableFuture.get(60, TimeUnit.SECONDS);

  assertNotNull(receivedGame);
  
}


}
