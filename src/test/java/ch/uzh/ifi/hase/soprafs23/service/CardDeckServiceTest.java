package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.CardDeck;
import ch.uzh.ifi.hase.soprafs23.entity.CardDrawResponse;

@SpringBootTest
public class CardDeckServiceTest {

    private CardDeck mockCardDeck;
    private Card[] mockCards;
    private Card mockCard1;
    private Card mockCard2;
    private CardDrawResponse mockCardDrawResponse;
    
    @Mock
    private HttpClient httpClient;

    @Mock 
    private ObjectMapper objectMapper;

    @InjectMocks
    private CardDeckService cardDeckService;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() throws IOException, InterruptedException {
        mockCardDeck = new CardDeck();
        mockCardDeck.setDeck_id("testDeck_id");
        mockCardDeck.setRemaining(52);
        mockCardDeck.setShuffled(true);
        mockCardDeck.setSuccess(true);

        mockCards = new Card[2];
        mockCard1 = new Card();
        mockCard1.setCode("6H");
        mockCard2 = new Card();
        mockCard2.setCode("8S");
        mockCards[0] = mockCard1;
        mockCards[1] = mockCard2;

        mockCardDrawResponse = new CardDrawResponse();
        mockCardDrawResponse.setCards(mockCards);

        HttpResponse httpResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any(HttpResponse.BodyHandlers.ofString().getClass()))).thenReturn(httpResponse);
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn("Hello");

        Mockito.when(objectMapper.readValue(Mockito.anyString(), Mockito.eq(CardDeck.class))).thenReturn(mockCardDeck);
        Mockito.when(objectMapper.readValue(Mockito.anyString(), Mockito.eq(CardDrawResponse.class))).thenReturn(mockCardDrawResponse);
    }

    @Test
    public void createCardDeck_success() throws IOException, InterruptedException {
        CardDeck newCardDeck = cardDeckService.createDeck();

        assertEquals(mockCardDeck.getDeck_id(), newCardDeck.getDeck_id());
        assertEquals(mockCardDeck.getRemaining(), newCardDeck.getRemaining());
        assertEquals(mockCardDeck.getShuffled(), newCardDeck.getShuffled());
        assertEquals(mockCardDeck.getSuccess(), newCardDeck.getSuccess());
    }

    @Test
    public void cardDraw_success() throws IOException, InterruptedException {
        Card[] newCards = cardDeckService.drawCards(mockCardDeck, 2);

        assertEquals(newCards[0], mockCards[0]);
        assertEquals(newCards[1], mockCards[1]);
    }

    @Test
    public void shuffleDeck_success() throws IOException, InterruptedException {
        CardDeck newCardDeck = cardDeckService.createDeck();

        assertEquals(mockCardDeck.getDeck_id(), newCardDeck.getDeck_id());
        assertEquals(mockCardDeck.getRemaining(), newCardDeck.getRemaining());
        assertEquals(mockCardDeck.getShuffled(), newCardDeck.getShuffled());
        assertEquals(mockCardDeck.getSuccess(), newCardDeck.getSuccess());
    }
}
