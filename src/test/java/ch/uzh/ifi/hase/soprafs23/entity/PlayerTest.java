package ch.uzh.ifi.hase.soprafs23.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import ch.uzh.ifi.hase.soprafs23.constant.Role;

@SpringBootTest
public class PlayerTest {
    private Player player;

    private User mockUser = new User();
    private Card card1 = new Card();
    private Card card2 = new Card();
    private List<Card> cards1 = new ArrayList<>();
    private List<Card> cards2 = new ArrayList<>();
    private ArrayList<Card> cardsInHand = new ArrayList<Card>();
    private ArrayList<Card> cardsInDiscard = new ArrayList<Card>();

    @BeforeEach
    public void setup() {
        mockUser.setUserId(1L);
        mockUser.setUsername("testUsername");

        card1.setCode("5H");
        card1.setSuit("HEARTS");
        card2.setCode("8S");
        card2.setSuit("SPADES");

        cardsInHand.add(card1);
        cardsInDiscard.add(card2);

        cards1.add(card1);
        cards2.add(card2);

        player = new Player(mockUser, Role.GUEST);
    }

    @Test
    public void getValues_success() {
        player.addCardsToHand(cards1);
        player.addCardsToDiscard(cards2);

        assertEquals(player.getPlayer(), mockUser);
        assertEquals(player.getHandSize(), 1);
        assertEquals(player.getDiscardPileSize(), 1);
        assertEquals(player.getCardsInHand().get(0).getCode(), cardsInHand.get(0).getCode());
        assertEquals(player.getCardsInDiscard().get(0).getCode(), cardsInDiscard.get(0).getCode());
    }

    @Test
    public void removeCardFromHand_success() {
        player.addCardsToHand(cards1);
        player.addCardsToHand(cards2);

        player.removeCardFromHand(card1);

        assertEquals(player.getHandSize(), 1);
        assertEquals(player.getCardsInHand().get(0).getCode(), card2.getCode());
    }

    @Test
    public void countDiscard_allPoints() {
        // create cards required to get all the points
        List<Card> winningCards = new ArrayList<>();
        // add 26 Clubs cards to the list
        for (int i = 0; i < 26; i++) {
            winningCards.add(new Card());
            winningCards.get(i).setSuit("CLUBS");
        }
        // add the two special points cards to the list
        Card twoOfClubs = new Card();
        twoOfClubs.setSuit("CLUBS");
        twoOfClubs.setCode("2C");
        Card tenOfDiamonds = new Card();
        tenOfDiamonds.setCode("10D");

        winningCards.add(twoOfClubs);
        winningCards.add(tenOfDiamonds);

        // add the cards to the player's discard pile
        player.addCardsToDiscard(winningCards);

        // count the points for the player
        int playerPoints = player.countDiscard();

        // check that the player got all the points
        assertEquals(5, playerPoints);
    }

    @Test
    public void countDiscard_noPoints() {

        player.addCardsToDiscard(cardsInDiscard);
        // no cards added so player will get no points
        int playerPoints = player.countDiscard();
        assertEquals(0, playerPoints);
    }

}
