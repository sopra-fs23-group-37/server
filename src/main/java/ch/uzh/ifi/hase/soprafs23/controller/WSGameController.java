package ch.uzh.ifi.hase.soprafs23.controller;

import java.io.IOException;

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

    @MessageMapping("/join/{gameId}")
    @SendTo("/topic/game/{gameId}")
    public Game join(SimpMessageHeaderAccessor headerAccessor, @DestinationVariable Long gameId, PlayerJoinMessage message) throws IOException, InterruptedException {
        Game game = this.gameService.websocketJoin(gameId, message.getPlayerId());
        return game;
    }


    @MessageMapping("/start/{gameId}")
    @SendTo("/topic/game/{gameId}")
    public Game start(SimpMessageHeaderAccessor headerAccessor, @DestinationVariable Long gameId) throws IOException, InterruptedException {
        Game game = this.gameService.startGame(gameId);
        return game;
    }

}
