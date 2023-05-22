package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.constant.RoundStatus;

@Entity
public class Round implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long roundId;

    @Column
    private Role currentTurnPlayer;

    @ManyToMany(targetEntity = Card.class)
    private List<Card> cardsOnTable = new ArrayList<Card>();

    @OneToOne(targetEntity = Player.class)
    private Player host;

    @OneToOne(targetEntity = Player.class)
    private Player guest;

    @OneToOne(targetEntity = CardDeck.class)
    private CardDeck cardDeck;

    @Column
    private RoundStatus roundStatus;

    @Column
    private int hostPoints;

    @Column
    private int guestPoints;

    @Column
    private Role lastCardGrab;

    private boolean guestConfirmedEOR = false;

    private boolean hostConfirmedEOR = false;

    public boolean getGuestConfirmedEOR() {
        return guestConfirmedEOR;
    }

    public void setGuestConfirmedEOR(boolean guestConfirmedEOR) {
        this.guestConfirmedEOR = guestConfirmedEOR;
    }

    public boolean getHostConfirmedEOR() {
        return hostConfirmedEOR;
    }

    public void setHostConfirmedEOR(boolean hostConfirmedEOR) {
        this.hostConfirmedEOR = hostConfirmedEOR;
    }

    public Role getLastCardGrab() {
        return lastCardGrab;
    }

    public void setLastCardGrab(Role lastCardGrab) {
        this.lastCardGrab = lastCardGrab;
    }

    public int getHostPoints() {
        return hostPoints;
    }

    public void setHostPoints(int hostPoints) {
        this.hostPoints = hostPoints;
    }

    public int getGuestPoints() {
        return guestPoints;
    }

    public void setGuestPoints(int guestPoints) {
        this.guestPoints = guestPoints;
    }

    public RoundStatus getRoundStatus() {
        return roundStatus;
    }

    public void setRoundStatus(RoundStatus roundStatus) {
        this.roundStatus = roundStatus;
    }

    public CardDeck getCardDeck() {
        return cardDeck;
    }

    public void setCardDeck(CardDeck cardDeck) {
        this.cardDeck = cardDeck;
    }

    public Role getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }

    public void setCurrentTurnPlayer(Role currentTurnPlayer) {
        this.currentTurnPlayer = currentTurnPlayer;
    }

    public List<Card> getTableCards() {
        return cardsOnTable;
    }

    public void addCardsToTable(List<Card> cards) {
        cardsOnTable.addAll(cards);
    }

    public void addCardToTable(Card card) {
        cardsOnTable.add(card);
    }

    public void removeCardFromTable(Card card) {
        cardsOnTable.removeIf(n -> (n.getCode().equals(card.getCode())));
    }

    public void removeAllCardsFromTable() {
        cardsOnTable.clear();
    }

    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public Player getHost() {
        return host;
    }

    public void setHost(Player host) {
        this.host = host;
    }

    public Player getGuest() {
        return guest;
    }

    public void setGuest(Player guest) {
        this.guest = guest;
    }

}
