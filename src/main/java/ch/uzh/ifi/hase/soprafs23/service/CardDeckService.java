package ch.uzh.ifi.hase.soprafs23.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.CardDeck;
import ch.uzh.ifi.hase.soprafs23.entity.CardDrawResponse;

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

    public Card[] drawCards(CardDeck deck, int number) throws IOException, InterruptedException {
        String uri = String.format("https://deckofcardsapi.com/api/deck/%s/draw/?count=%s", deck.getDeck_id(), number);
		// build get request and get deck id

		HttpRequest request = HttpRequest.newBuilder()
			.GET()
			.uri(URI.create(uri))
			.build();
		
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		CardDrawResponse cardDrawResponse = objectMapper.readValue(response.body(), CardDrawResponse.class);

        return cardDrawResponse.getCards();
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
