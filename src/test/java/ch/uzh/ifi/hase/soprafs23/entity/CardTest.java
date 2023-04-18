package ch.uzh.ifi.hase.soprafs23.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CardTest {
  private Card card = new Card();
  private Images images = new Images();

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    card.setCode("testCode");
    card.setImage("testImage");
    card.setImages(images);
    card.setSuit("testSuit");
    card.setValue("testValue");
  }

  @Test
  public void createCard_validInputs() {
    assertEquals("testCode", card.getCode());
    assertEquals("testImage", card.getImage());
    assertEquals(card.getImages(), images);
    assertEquals("testSuit", card.getSuit());
    assertEquals("testValue", card.getValue());
  }
}
