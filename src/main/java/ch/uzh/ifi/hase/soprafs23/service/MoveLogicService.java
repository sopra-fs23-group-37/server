package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerMoveMessage;
import ch.uzh.ifi.hase.soprafs23.entity.Round;

// we assume a move has player, cards from hand + cards from field
// maybe needs a field to define type of move (joker, normal, double, put down)
public class MoveLogicService {
    public Game checkMove(Game game, PlayerMoveMessage message) {
        Round currentRound = game.getCurrentRound();
        return game;
    }
}
