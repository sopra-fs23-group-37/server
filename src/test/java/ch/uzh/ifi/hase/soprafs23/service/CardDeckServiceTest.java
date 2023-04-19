package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.CardDeck;
import ch.uzh.ifi.hase.soprafs23.entity.CardDrawResponse;
import ch.uzh.ifi.hase.soprafs23.repository.CardDeckRepository;
import ch.uzh.ifi.hase.soprafs23.repository.CardRepository;

@SpringBootTest
public class CardDeckServiceTest {

    private CardDeck mockCardDeck;
    private List<Card> mockCards;
    private Card mockCard1;
    private Card mockCard2;
    private CardDrawResponse mockCardDrawResponse;

    @Mock
    private HttpClient httpClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpResponse<String> httpResponse;

    @MockBean
    private CardDeckRepository cardDeckRepository;

    @MockBean
    private CardRepository cardRepository;

    @InjectMocks
    private CardDeckService cardDeckService;

    @BeforeEach
    public void setup() throws IOException, InterruptedException {
        MockitoAnnotations.openMocks(this);

        mockCardDeck = new CardDeck();
        mockCardDeck.setCardDeckId(1L);
        mockCardDeck.setDeck_id("testDeck_id");
        mockCardDeck.setRemaining(52);
        mockCardDeck.setShuffled(true);
        mockCardDeck.setSuccess(true);

        mockCards = new ArrayList<Card>();

        mockCard1 = new Card();
        mockCard1.setCode("6H");
        mockCard2 = new Card();
        mockCard2.setCode("8S");
        mockCards.add(mockCard1);
        mockCards.add(mockCard2);

        mockCardDrawResponse = new CardDrawResponse();
        mockCardDrawResponse.setCards(mockCards);

        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn("Hello");

        Mockito.when(httpClient.send(Mockito.any(HttpRequest.class), Mockito.eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(httpResponse);

        Mockito.when(objectMapper.readValue(Mockito.anyString(), Mockito.eq(CardDeck.class))).thenReturn(mockCardDeck);
        Mockito.when(objectMapper.readValue(Mockito.anyString(), Mockito.eq(CardDrawResponse.class)))
                .thenReturn(mockCardDrawResponse);

        Mockito.when(cardDeckRepository.save(Mockito.any(CardDeck.class))).thenReturn(mockCardDeck);
        Mockito.when(cardRepository.saveAll(Mockito.any())).thenReturn(mockCards);
    }

    @Test
    public void createCardDeck_success() throws IOException, InterruptedException {
        CardDeck newCardDeck = cardDeckService.createDeck();
        System.out.println(newCardDeck.getDeck_id());

        assertEquals(mockCardDeck.getDeck_id(), newCardDeck.getDeck_id());
        assertEquals(mockCardDeck.getRemaining(), newCardDeck.getRemaining());
        assertEquals(mockCardDeck.getShuffled(), newCardDeck.getShuffled());
        assertEquals(mockCardDeck.getSuccess(), newCardDeck.getSuccess());
    }

    @Test
    public void cardDraw_success() throws IOException, InterruptedException {
        List<Card> newCards = new ArrayList<>();
        newCards = cardDeckService.drawCards(mockCardDeck, 2);

        assertEquals(newCards.get(0), mockCards.get(0));
        assertEquals(newCards.get(1), mockCards.get(1));
    }

    @Test
    public void shuffleDeck_success() throws IOException, InterruptedException {
        CardDeck newCardDeck = cardDeckService.shuffleDeck(mockCardDeck);

        assertEquals(mockCardDeck.getDeck_id(), newCardDeck.getDeck_id());
        assertEquals(mockCardDeck.getRemaining(), newCardDeck.getRemaining());
        assertEquals(mockCardDeck.getShuffled(), newCardDeck.getShuffled());
        assertEquals(mockCardDeck.getSuccess(), newCardDeck.getSuccess());
    }

}
