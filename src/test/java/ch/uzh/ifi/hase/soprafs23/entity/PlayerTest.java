package ch.uzh.ifi.hase.soprafs23.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PlayerTest {
    private Player player;
    
    private User mockUser = new User();
    private Card card1 = new Card();
    private Card card2 = new Card();
    private ArrayList<Card> cardsInHand = new ArrayList<Card>();
    private ArrayList<Card> cardsInDiscard = new ArrayList<Card>();

    @BeforeEach
    public void setup() {
        mockUser.setUserId(1L);
        mockUser.setUsername("testUsername");

        card1.setCode("5H");
        card2.setCode("8S");

        cardsInHand.add(card1);
        cardsInDiscard.add(card2);
        
        player = new Player(mockUser);
    }

    @Test
    public void getValues_success() {
        player.addCardToHand(card1);
        player.addCardToDiscard(card2);
        
        assertEquals(player.getPlayer(), mockUser);
        assertEquals(player.getHandSize(), 1);
        assertEquals(player.getDiscardPileSize(), 1);
        assertEquals(player.getCardsInHand().get(0).getCode(), cardsInHand.get(0).getCode());
        assertEquals(player.getCardsInDiscard().get(0).getCode(), cardsInDiscard.get(0).getCode());
    }

    @Test 
    public void removeCardFromHand_success() {
        player.addCardToHand(card1);
        player.addCardToHand(card2);

        player.removeCardFromHand(card1);

        assertEquals(player.getHandSize(), 1);
        assertEquals(player.getCardsInHand().get(0).getCode(), card2.getCode());
    }
}
