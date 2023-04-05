package ch.uzh.ifi.hase.soprafs23.entity;

import java.util.ArrayList;

public class Player {
    private User player;

    private ArrayList<Card> cardsInHand = new ArrayList<Card>();
    private ArrayList<Card> cardsInDiscard = new ArrayList<Card>();

    public void addCardToHand(Card card) {
        cardsInHand.add(card);
    }

    public void addCardToDiscard(Card card) {
        cardsInDiscard.add(card);
    }

    public void removeCardFromHand(Card card) {
        cardsInHand.removeIf(n -> (n.getCode().equals(card.getCode())));
    }

    public Player(User player) {
        this.player = player;
    }

    public int getHandSize() {
        return cardsInHand.size();
    }

    public int getDiscardPileSize() {
        return cardsInDiscard.size();
    }

    public User getPlayer() {
        return player;
    }

    public ArrayList<Card> getCardsInDiscard() {
        return cardsInDiscard;
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }
}
