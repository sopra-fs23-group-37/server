package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import ch.uzh.ifi.hase.soprafs23.constant.Role;

@Entity
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long playerId;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private User player;

    @Column
    @Lob
    private ArrayList<Card> cardsInHand = new ArrayList<Card>();

    @Column
    @Lob
    private ArrayList<Card> cardsInDiscard = new ArrayList<Card>();

    @Column
    private Role role;

    @Column
    private int pointsTotalCards = 0;

    @Column
    private int pointClubs = 0;

    @Column
    private int twoOfClubs = 0;

    @Column
    private int tenOfDiamonds = 0;

    @Column
    private int totalPoints = 0;

    public int getPointsTotalCards() {
        return pointsTotalCards;
    }

    public int getPointClubs() {
        return pointClubs;
    }

    public int getTwoOfClubs() {
        return twoOfClubs;
    }

    public int getTenOfDiamonds() {
        return tenOfDiamonds;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public Player() {
    }

    public Player(User player, Role role) {
        this.player = player;
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void addCardsToHand(List<Card> cards) {
        cardsInHand.addAll(cards);
    }

    public void addCardToDiscard(Card card) {
        cardsInDiscard.add(card);
    }

    public void removeCardFromHand(Card card) {
        cardsInHand.removeIf(n -> (n.getCode().equals(card.getCode())));
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

    // count the cards in the discard
    public int countDiscard() {

        // award points if more than half the total cards
        this.pointsTotalCards = this.cardsInDiscard.size() > 26 ? 2 : 0;

        // count clubs and detect special cards
        int clubs = 0;
        for (Card card : this.cardsInDiscard) {
            // count total number of clubs
            if (card.getSuit().equals("CLUBS")) {
                clubs++;

                // check if player has the 2 of Clubs
                if (card.getCode().equals("2C")) {
                    this.twoOfClubs = 1;
                }
                // check if player has the 10 of Diamonds
            } else if (card.getCode().equals("10D")) {
                this.tenOfDiamonds = 1;
            }
        }
        // award points if more than half the clubs cards
        this.pointClubs = clubs >= 7 ? 1 : 0;

        // sum up the total points
        this.totalPoints = this.pointsTotalCards + this.pointClubs + this.twoOfClubs + this.tenOfDiamonds;

        return this.totalPoints;
    }

}
