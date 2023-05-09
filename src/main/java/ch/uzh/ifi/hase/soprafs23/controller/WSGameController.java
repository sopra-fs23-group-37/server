package ch.uzh.ifi.hase.soprafs23.controller;

import java.io.IOException;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerJoinMessage;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerMoveMessage;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import ch.uzh.ifi.hase.soprafs23.service.WebsocketService;

@Controller
public class WSGameController {

    private final GameService gameService;
    private final UserService userService;
    private final WebsocketService websocketService;

    WSGameController(GameService gameService, UserService userService, WebsocketService websocketService) {
        this.gameService = gameService;
        this.userService = userService;
        this.websocketService = websocketService;
    }

    @MessageMapping("/home")
    public void sendUpdates(PlayerJoinMessage message) {
        this.gameService.sendPublicGamesUpdate();
        User user = this.userService.getUserById(message.getPlayerId());
        this.websocketService.sendStatsUpdateToUser(user);

    }

    @MessageMapping("/join/{gameId}")
    @SendTo("/topic/game/{gameId}")
    public Game join(@Header("simpSessionId") String sessionId, @DestinationVariable Long gameId,
            PlayerJoinMessage message) throws IOException, InterruptedException {
        return this.gameService.websocketJoin(gameId, message.getPlayerId(), sessionId);
    }

    @MessageMapping("/start/{gameId}")
    @SendTo("/topic/game/{gameId}")
    public Game start(@DestinationVariable Long gameId, PlayerJoinMessage message)
            throws IOException, InterruptedException {
        return this.gameService.startGame(gameId, message.getPlayerId());
    }

    @MessageMapping("/move/{gameId}")
    @SendTo("/topic/game/{gameId}")
    public Game move(SimpMessageHeaderAccessor headerAccessor, @DestinationVariable Long gameId,
            PlayerMoveMessage message) throws IOException, InterruptedException {
        return this.gameService.makeMove(gameId, message);
    }

    @MessageMapping("/reconnect/{gameId}")
    public void reconnect(@Header("simpSessionId") String sessionId, @DestinationVariable Long gameId,
            PlayerJoinMessage message) throws IOException, InterruptedException {
        this.gameService.reconnect(gameId, message.getPlayerId(), sessionId);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        // send the disconnected session to the game service to manage
        String sessionId = event.getSessionId();
        gameService.findDisconnectedPlayer(sessionId);
    }

    @MessageMapping("/surrender/{gameId}")
    public void surrender(@DestinationVariable Long gameId,
            PlayerJoinMessage message) {
        this.gameService.surrender(gameId, message.getPlayerId());
    }

    @MessageMapping("/confirmEOR/{gameId}")
    public void confirmEOR(SimpMessageHeaderAccessor headerAccessor, @DestinationVariable Long gameId,
            PlayerJoinMessage message) throws IOException, InterruptedException {
        this.gameService.confirmEOR(gameId, message.getPlayerId());
    }

}
