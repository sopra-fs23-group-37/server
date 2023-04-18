package ch.uzh.ifi.hase.soprafs23.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CardDrawResponseTest {
  private CardDrawResponse cardDrawResponse = new CardDrawResponse();
  private Card[] cards = new Card[2];

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    Card card1 = new Card();
    Card card2 = new Card();

    cards[0] = card1;
    cards[1] = card2;

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