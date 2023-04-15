package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import ch.uzh.ifi.hase.soprafs23.constant.Role;

@Entity
public class Round implements Serializable {

     private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long roundId;

    private Role currentTurnPlayer;

    @OneToMany(targetEntity = Card.class)
    private List<Card> cardsOnTable = new ArrayList<Card>();
    
    @OneToMany(targetEntity = Player.class)
    private List<Player> players = new ArrayList<Player>();;

    public void addPlayer(Player player) {
        players.add(player);
    }

   public List<Player> getPlayers() {
    return players;
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

    public void removeCardFromTable(Card card) {
        cardsOnTable.removeIf(n -> (n.getCode().equals(card.getCode())));
    }
   
       public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    
}
