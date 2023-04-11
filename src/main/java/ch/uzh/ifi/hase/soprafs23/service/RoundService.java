package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.CardDeck;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Round;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoundRepository;
import ch.uzh.ifi.hase.soprafs23.entity.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class RoundService {
    
    private final RoundRepository roundRepository;
    private final PlayerRepository playerRepository;

        
    private final CardDeckService cardDeckService;

    @Autowired
    RoundService(
        @Qualifier("roundRepository") RoundRepository roundRepository, 
        @Qualifier("playerRepository") PlayerRepository playerRepository, 
        CardDeckService cardDeckService) {
        this.roundRepository = roundRepository;
        this.playerRepository = playerRepository;
        this.cardDeckService = cardDeckService;
    }


    public Round newRound(Game game) throws IOException, InterruptedException {

        // create the new round
        Round round =  new Round();

        // add a shuffled deck to the round
        CardDeck deck = cardDeckService.createShuffledDeck();
        round.setCardDeck(deck);
        
        // create player hands, deal 8 cards to each
        Player host = new Player(game.getHost(), Role.HOST);
        dealEightCards(host, deck);
        
        Player guest = new Player(game.getGuest(), Role.GUEST);
        dealEightCards(guest, deck);

        // add players to round and set starting turn
        round.addPlayer(host);
        round.addPlayer(guest);
        round.setCurrentTurnPlayer(determineStartTurn(game));

        // add 4 cards to table, save 
        dealFourCardsTable(round, deck);
        round = roundRepository.save(round);        
        roundRepository.flush();

        // return the new round
        return round;
    };


    public Role determineStartTurn(Game game) {
        // 
        if (game.getTotalRounds() % 2 == 0) {
            return game.getStartingPlayer();
        } else if (game.getStartingPlayer() == Role.HOST) {
            return Role.GUEST;
        } else {
            return Role.HOST;
        }
    }

    public void dealEightCards(Player player, CardDeck deck) throws IOException, InterruptedException {
        List<Card> cards = cardDeckService.drawCards(deck, 8);
        player.addCardsToHand(cards);
        player = playerRepository.save(player);
        playerRepository.flush();
    }

    public void dealFourCardsTable(Round round, CardDeck deck) throws IOException, InterruptedException {
        List<Card> tableCards = cardDeckService.drawCards(deck, 4);
        round.addCardsToTable(tableCards);
    }

}
