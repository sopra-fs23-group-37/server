package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs23.constant.RoundStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Card;

public class WSRoundStatusDTO {

    // status
    private RoundStatus roundStatus;

    // players points
    private int myPointsTotalCards;
    private int myPointClubs;
    private int myTwoOfClubs;
    private int myTenOfDiamonds;
    private int myTotalPoints;

    // opponent points
    private int oppPointsTotalCards;
    private int oppPointClubs;
    private int oppTwoOfClubs;
    private int oppTenOfDiamonds;
    private int oppTotalPoints;

    // cards
    private ArrayList<Card> myCardsInHand = new ArrayList<Card>();
    private ArrayList<Card> myCardsInDiscard = new ArrayList<Card>();
    private int oppCards;
    private ArrayList<Card> oppCardsInDiscard = new ArrayList<Card>();
    private List<Card> cardsOnTable = new ArrayList<Card>();
    private Boolean deckCards;

    // turn
    private Boolean myTurn;

    // manage player leaving
    private Boolean opponentLeft;
    private String opponentLeftReason;

    public RoundStatus getRoundStatus() {
        return roundStatus;
    }

    public void setRoundStatus(RoundStatus roundStatus) {
        this.roundStatus = roundStatus;
    }

    public int getMyPointsTotalCards() {
        return myPointsTotalCards;
    }

    public void setMyPointsTotalCards(int myPointsTotalCards) {
        this.myPointsTotalCards = myPointsTotalCards;
    }

    public int getMyPointClubs() {
        return myPointClubs;
    }

    public void setMyPointClubs(int myPointClubs) {
        this.myPointClubs = myPointClubs;
    }

    public int getMyTwoOfClubs() {
        return myTwoOfClubs;
    }

    public void setMyTwoOfClubs(int myTwoOfClubs) {
        this.myTwoOfClubs = myTwoOfClubs;
    }

    public int getMyTenOfDiamonds() {
        return myTenOfDiamonds;
    }

    public void setMyTenOfDiamonds(int myTenOfDiamonds) {
        this.myTenOfDiamonds = myTenOfDiamonds;
    }

    public int getMyTotalPoints() {
        return myTotalPoints;
    }

    public void setMyTotalPoints(int myTotalPoints) {
        this.myTotalPoints = myTotalPoints;
    }

    public int getOppPointsTotalCards() {
        return oppPointsTotalCards;
    }

    public void setOppPointsTotalCards(int oppPointsTotalCards) {
        this.oppPointsTotalCards = oppPointsTotalCards;
    }

    public int getOppPointClubs() {
        return oppPointClubs;
    }

    public void setOppPointClubs(int oppPointClubs) {
        this.oppPointClubs = oppPointClubs;
    }

    public int getOppTwoOfClubs() {
        return oppTwoOfClubs;
    }

    public void setOppTwoOfClubs(int oppTwoOfClubs) {
        this.oppTwoOfClubs = oppTwoOfClubs;
    }

    public int getOppTenOfDiamonds() {
        return oppTenOfDiamonds;
    }

    public void setOppTenOfDiamonds(int oppTenOfDiamonds) {
        this.oppTenOfDiamonds = oppTenOfDiamonds;
    }

    public int getOppTotalPoints() {
        return oppTotalPoints;
    }

    public void setOppTotalPoints(int oppTotalPoints) {
        this.oppTotalPoints = oppTotalPoints;
    }

    public ArrayList<Card> getMyCardsInHand() {
        return myCardsInHand;
    }

    public void setMyCardsInHand(ArrayList<Card> myCardsInHand) {
        this.myCardsInHand = myCardsInHand;
    }

    public ArrayList<Card> getMyCardsInDiscard() {
        return myCardsInDiscard;
    }

    public void setMyCardsInDiscard(ArrayList<Card> myCardsInDiscard) {
        this.myCardsInDiscard = myCardsInDiscard;
    }

    public int getOppCards() {
        return oppCards;
    }

    public void setOppCards(int oppCards) {
        this.oppCards = oppCards;
    }

    public ArrayList<Card> getOppCardsInDiscard() {
        return oppCardsInDiscard;
    }

    public void setOppCardsInDiscard(ArrayList<Card> oppCardsInDiscard) {
        this.oppCardsInDiscard = oppCardsInDiscard;
    }

    public List<Card> getCardsOnTable() {
        return cardsOnTable;
    }

    public void setCardsOnTable(List<Card> cardsOnTable) {
        this.cardsOnTable = cardsOnTable;
    }

    public Boolean getDeckCards() {
        return deckCards;
    }

    public void setDeckCards(Boolean deckCards) {
        this.deckCards = deckCards;
    }

    public Boolean getMyTurn() {
        return myTurn;
    }

    public void setMyTurn(Boolean myTurn) {
        this.myTurn = myTurn;
    }

    public Boolean getOpponentLeft() {
        return opponentLeft;
    }

    public void setOpponentLeft(Boolean opponentLeft) {
        this.opponentLeft = opponentLeft;
    }

    public String getOpponentLeftReason() {
        return opponentLeftReason;
    }

    public void setOpponentLeftReason(String opponentLeftReason) {
        this.opponentLeftReason = opponentLeftReason;
    }

}
