package ch.uzh.ifi.hase.soprafs23.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerJoinMessage;
import ch.uzh.ifi.hase.soprafs23.service.GameService;


@Controller
public class WSGameController {

    private final GameService gameService;

    WSGameController(GameService gameService) {
        this.gameService = gameService;
    }

    @MessageMapping("/join/{id}")
    @SendTo("/topic/game/{id}")
    public Game join(SimpMessageHeaderAccessor headerAccessor, @DestinationVariable Long id, PlayerJoinMessage message) {
        Game game = this.gameService.websocketJoin(id, message.getPlayer());
        return game;
    }
}
