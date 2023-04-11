package ch.uzh.ifi.hase.soprafs23.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import ch.uzh.ifi.hase.soprafs23.constant.Role;

@SpringBootTest
public class RoundTest {
    private Round mockRound = new Round();
    private User mockHost = new User();
    private User mockGuest = new User();
    private Player host;
    private Player guest;
    private Card card1 = new Card();
    private Card card2 = new Card();
    private List<Card> cards1 = new ArrayList<>();
    private List<Card> cards2 = new ArrayList<>();
    private ArrayList<Card> cardsOnTable = new ArrayList<Card>();

    @BeforeEach
    public void setup() {
        // set up the mock round players
        mockHost.setUserId(1L);
        mockHost.setUsername("testHost");
        mockGuest.setUserId(2L);
        mockGuest.setUsername("testGuest");
        host = new Player(mockHost, Role.HOST);
        guest = new Player(mockGuest, Role.GUEST);

        // set up cards to add during test
        card1.setCode("5H");
        card2.setCode("8S");
        cardsOnTable.add(card1);
        cards1.add(card1);
        cards2.add(card2);

        // set the current turn player
        mockRound.setCurrentTurnPlayer(Role.HOST);
    }

    @Test
    public void getValues_success() {
        // add cards and players to the round
        mockRound.addCardsToTable(cardsOnTable);
        mockRound.addPlayer(host);
        mockRound.addPlayer(guest);
        
        // check that they have been added correctly and can be accessed
        assertEquals(mockRound.getTableCards().get(0).getCode(), cardsOnTable.get(0).getCode());
        assertEquals(mockRound.getCurrentTurnPlayer(), Role.HOST);
        assertEquals(mockRound.getPlayers().get(0), host);
        assertEquals(mockRound.getPlayers().get(1), guest);
    }

    @Test 
    public void removeCardFromTable_success() {
        mockRound.addCardsToTable(cards1);
        mockRound.addCardsToTable(cards2);
        mockRound.removeCardFromTable(card1);

        assertEquals(mockRound.getTableCards().get(0).getCode(), card2.getCode());
    }
}
