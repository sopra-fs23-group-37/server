package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerMoveMessage;
import ch.uzh.ifi.hase.soprafs23.entity.Round;

// we assume a move has player, cards from hand + cards from field
// maybe needs a field to define type of move (joker, normal, double, put down)

// for now
// 1: 1-1, 2: x-1, 3: JACK, 4: to field

// decide to work with booleans or error
public class MoveLogicService {
    // booleans or make gamestate class?
    public Game checkMove(Game game, PlayerMoveMessage message) {
        Round currentRound = game.getCurrentRound();

        switch (message.getMoveType()) {
            case 1:
                checkMove1(message);
            case 2:
                checkMove2(message);
            case 3:
                checkMove3(message);
            case 4: 
                checkMove4(message);
        }
        return game;
    }

    // 1-1
    public boolean checkMove1(PlayerMoveMessage message) {
        Card playerCard = message.getCardFromHand();

        if (message.getCardsFromField().size() != 1) {
            return false;
        }

        Card fieldCard = message.getCardsFromField().get(0);

        if (playerCard.getValue().equals(fieldCard.getValue())) {
            return true;
        }

        return false;
    }

    // x-1
    public boolean checkMove2(PlayerMoveMessage message) {
        Card playerCard = message.getCardFromHand();

        if (message.getCardsFromField().size() <= 1) {
            return false;    
        }

        // scuffed bc value is stored as string by api
        // needs a check if picture cards are used
        int total = 0; 
        for (Card c: message.getCardsFromField() ) {
            total += Integer.parseInt(c.getValue());
        }

        if (Integer.toString(total).equals(playerCard.getValue())) {
            return true;
        }

        return false;
    }

    // JACK
    public boolean checkMove3(PlayerMoveMessage message) {
        Card playeCard = message.getCardFromHand();

        if (playeCard.getValue().equals("JACK")) {
            return true;
        }

        return false;
    }

    // to field
    public boolean checkMove4(PlayerMoveMessage message) {
        return true;
    }
}
