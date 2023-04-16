package ch.uzh.ifi.hase.soprafs23.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.CardDeck;
import ch.uzh.ifi.hase.soprafs23.entity.CardDrawResponse;
import ch.uzh.ifi.hase.soprafs23.repository.CardDeckRepository;
import ch.uzh.ifi.hase.soprafs23.repository.CardRepository;

@Service
@Transactional
public class CardDeckService {
	private HttpClient httpClient; ;
	private ObjectMapper objectMapper;

	private CardDeckRepository cardDeckRepository;
	private CardRepository cardRepository;

	CardDeckService(
		@Qualifier("cardDeckRepository") CardDeckRepository cardDeckRepository,
		@Qualifier("cardRepository") CardRepository cardRepository) {
		this.cardDeckRepository = cardDeckRepository;
		this.cardRepository = cardRepository;
		this.httpClient = HttpClient.newHttpClient();
		this.objectMapper = new ObjectMapper();
	}

    public CardDeck createDeck() throws IOException, InterruptedException {
        String uri = "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1";

		// build get request and get deck id
		
		HttpRequest request = HttpRequest.newBuilder()
			.GET()
			.uri(URI.create(uri))
			.build();
			
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		CardDeck newDeck = objectMapper.readValue(response.body(), CardDeck.class);

		newDeck = cardDeckRepository.save(newDeck);
		cardDeckRepository.flush();

        return newDeck;
    }

    public List<Card> drawCards(CardDeck deck, int number) throws IOException, InterruptedException {
        List<Card> cards = new ArrayList<>();
		String uri = String.format("https://deckofcardsapi.com/api/deck/%s/draw/?count=%s", deck.getDeck_id(), number);

		// build get request and get deck id
		HttpRequest request = HttpRequest.newBuilder()
			.GET()
			.uri(URI.create(uri))
			.build();
		
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		CardDrawResponse cardDrawResponse = objectMapper.readValue(response.body(), CardDrawResponse.class);
		
		cards = cardDrawResponse.getCards();
		
		cards = cardRepository.saveAll(cards);
		cardRepository.flush();

        return cards;
    }

	public CardDeck shuffleDeck(CardDeck deck) throws IOException, InterruptedException {
		String uri = String.format("https://deckofcardsapi.com/api/deck/%s/shuffle/", deck.getDeck_id());
		
		// build get request and get deck id
		HttpRequest request = HttpRequest.newBuilder()
			.GET()
			.uri(URI.create(uri))
			.build();
		
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		CardDeck deckResponse = objectMapper.readValue(response.body(), CardDeck.class);
		
		deckResponse = cardDeckRepository.save(deckResponse);
		cardDeckRepository.flush();

        return deckResponse;
	}
}
