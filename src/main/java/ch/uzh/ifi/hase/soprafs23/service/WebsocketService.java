package ch.uzh.ifi.hase.soprafs23.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WebsocketService {

    @Autowired
    private final SimpMessagingTemplate simp;

    public WebsocketService(SimpMessagingTemplate simp) {
        this.simp = simp;
    }

    /**
     * method to send a dto to the lobby channel of a specific game
     * 
     * @param gameId the id of the game to send the payload to
     * @param dto    any data object, should be a dto with only data necessary for
     *               the client
     */
    public void sendToLobby(Long gameId, Object dto) {
        String destination = String.format("/topic/game/%d/lobby", gameId);
        this.simp.convertAndSend(destination, dto);
    }

    /**
     * method to send a dto to the game channel of a specific game
     * 
     * @param gameId the id of the game to send the payload to
     * @param dto    any data object, should be a dto with only data necessary for
     *               the client
     */
    public void sendToGame(Long gameId, Object dto) {
        String destination = String.format("/topic/game/%d/game", gameId);
        this.simp.convertAndSend(destination, dto);
    }

    /**
     * method to send a dto to the round channel of a specific game
     * 
     * @param gameId the id of the game to send the payload to
     * @param dto    any data object, should be a dto with only data necessary for
     *               the client
     */
    public void sendToRound(Long gameId, Object dto) {
        String destination = String.format("/topic/game/%d/round", gameId);
        this.simp.convertAndSend(destination, dto);
    }

}
