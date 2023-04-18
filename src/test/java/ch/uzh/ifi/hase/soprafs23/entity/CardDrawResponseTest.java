package ch.uzh.ifi.hase.soprafs23.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CardDrawResponseTest {
  private CardDrawResponse cardDrawResponse = new CardDrawResponse();
  private List<Card> cards;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    cards = new ArrayList<Card>();

    Card card1 = new Card();
    Card card2 = new Card();

    cards.add(card1);
    cards.add(card2);

    cardDrawResponse.setCards(cards);
    cardDrawResponse.setDeck_id("testDeck_id");
    cardDrawResponse.setRemaining(52);
    cardDrawResponse.setSuccess(true);
  }

  @Test
  public void createImages_validInputs() {
    assertEquals(cardDrawResponse.getDeck_id(), "testDeck_id");
    assertEquals(cardDrawResponse.getRemaining(), 52);
    assertEquals(cardDrawResponse.getSuccess(), true);
    assertEquals(cardDrawResponse.getCards(), cards);
  }
}