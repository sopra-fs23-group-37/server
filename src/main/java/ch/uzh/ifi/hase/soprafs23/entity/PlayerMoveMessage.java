package ch.uzh.ifi.hase.soprafs23.entity;

import java.util.List;

public class PlayerMoveMessage {
    private long playerId;
    private int moveType;
    private Card cardFromHand;
    private List<Card> cardsFromField;
    
    public long getPlayerId() {
        return playerId;
    }
    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }
    public int getMoveType() {
        return moveType;
    }
    public void setMoveType(int moveType) {
        this.moveType = moveType;
    }
    public Card getCardFromHand() {
        return cardFromHand;
    }
    public void setCardFromHand(Card cardFromHand) {
        this.cardFromHand = cardFromHand;
    }
    public List<Card> getCardFromField() {
        return cardsFromField;
    }
    public void setCardFromField(List<Card> cardFromField) {
        this.cardsFromField = cardFromField;
    }
}
