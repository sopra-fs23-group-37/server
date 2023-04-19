package ch.uzh.ifi.hase.soprafs23.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CardDeckTest {
    private CardDeck cardDeck = new CardDeck();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        cardDeck.setDeck_id("testDeck_id");
        cardDeck.setRemaining(52);
        cardDeck.setShuffled(true);
        cardDeck.setSuccess(true);
    }

    @Test
    public void createImages_validInputs() {
        assertEquals("testDeck_id", cardDeck.getDeck_id());
        assertEquals(52, cardDeck.getRemaining());
        assertEquals(true, cardDeck.getShuffled());
        assertEquals(true, cardDeck.getSuccess());
    }
}