package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerMoveMessage;
import ch.uzh.ifi.hase.soprafs23.entity.Round;

// we assume a move has player, cards from hand + cards from field
// maybe needs a field to define type of move (joker, normal, double, put down)

// for now
// 1: 1-1, 2: x-1, 3: J, 4: to field

// decide to work with booleans or error
public class MoveLogicService {
    public Game checkMove(Game game, PlayerMoveMessage message) {
        Round currentRound = game.getCurrentRound();

        switch (message.getMoveType()) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4: 
                break;
        }
        return game;
    }

    public boolean checkMove1(PlayerMoveMessage message) {
        Card playerCard = message.getCardFromHand();

        if (message.getCardFromField().size() != 1) {
            return false;
        }

        Card fieldCard = message.getCardFromField().get(0);

        if (playerCard.getValue().equals(fieldCard.getValue())) {
            return true;
        }

        return false;
    }
}
