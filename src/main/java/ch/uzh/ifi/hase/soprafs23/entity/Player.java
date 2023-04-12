package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import ch.uzh.ifi.hase.soprafs23.constant.Role;

@Entity
public class Player implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long playerId;
    
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private User player;

    @Lob
    private ArrayList<Card> cardsInHand = new ArrayList<Card>();
    @Lob
    private ArrayList<Card> cardsInDiscard = new ArrayList<Card>();

    private Role role;

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

    public void addCardsToDiscard(List<Card> cards) {
        cardsInDiscard.addAll(cards);
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

}
