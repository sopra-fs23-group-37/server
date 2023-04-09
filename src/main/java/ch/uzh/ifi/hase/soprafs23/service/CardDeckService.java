package ch.uzh.ifi.hase.soprafs23.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.CardDeck;
import ch.uzh.ifi.hase.soprafs23.entity.CardDrawResponse;

@Service
@Transactional
public class CardDeckService {
	HttpClient httpClient = HttpClient.newHttpClient();
	ObjectMapper objectMapper = new ObjectMapper();

    public CardDeck createDeck() throws IOException, InterruptedException {
        String uri = "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1";
		// build get request and get deck id
		

		HttpRequest request = HttpRequest.newBuilder()
			.GET()
			.uri(URI.create(uri))
			.build();
			
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		CardDeck newDeck = objectMapper.readValue(response.body(), CardDeck.class);

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
		
		cards = Arrays.asList(cardDrawResponse.getCards());

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
		
        return deckResponse;
	}
}
