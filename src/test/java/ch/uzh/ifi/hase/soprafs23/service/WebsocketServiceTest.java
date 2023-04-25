package ch.uzh.ifi.hase.soprafs23.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class WebsocketServiceTest {

    private SimpMessagingTemplate simp;

    private WebsocketService websocketService;

    @BeforeEach
    public void setup() {
        simp = mock(SimpMessagingTemplate.class);
        websocketService = new WebsocketService(simp);
    }

    @Test
    public void testSendToLobby() {
        Long gameId = 1L;
        Object dto = new Object();

        websocketService.sendToLobby(gameId, dto);

        String destination = String.format("/topic/game/%d/lobby", gameId);
        verify(simp).convertAndSend(destination, dto);
    }

    @Test
    public void testSendToGame() {
        Long gameId = 1L;
        Object dto = new Object();

        websocketService.sendToGame(gameId, dto);

        String destination = String.format("/topic/game/%d/game", gameId);
        verify(simp).convertAndSend(destination, dto);
    }

}
