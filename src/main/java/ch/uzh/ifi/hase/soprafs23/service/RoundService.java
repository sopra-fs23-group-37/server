package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.CardDeck;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Round;
import ch.uzh.ifi.hase.soprafs23.repository.CardDeckRepository;
import ch.uzh.ifi.hase.soprafs23.repository.CardRepository;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoundRepository;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerMoveMessage;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class RoundService {

    
    private final RoundRepository roundRepository;
    private final PlayerRepository playerRepository;
    private final CardDeckRepository cardDeckRepository;
    private final CardRepository cardRepository;
    private final GameRepository gameRepository;
        
    private final CardDeckService cardDeckService;

    
    RoundService(RoundRepository roundRepository, PlayerRepository playerRepository, CardDeckRepository cardDeckRepository, CardRepository cardRepository, GameRepository gameRepository) {
        this.cardDeckService = new CardDeckService();
        this.roundRepository = roundRepository;
        this.playerRepository = playerRepository;
        this.cardDeckRepository = cardDeckRepository;
        this.cardRepository = cardRepository;
        this.gameRepository = gameRepository;
    }


    public Game newRound(Long gameId) throws IOException, InterruptedException {

        Game game = gameRepository.findByGameId(gameId);

        // create new deck, shuffle it, save
        CardDeck deck = cardDeckService.createDeck();
        cardDeckService.shuffleDeck(deck);
        deck = cardDeckRepository.save(deck);
        

        // create player hands, deal 8 cards to each, save 
        Player host = new Player(game.getHost());
        host.setRole(Role.HOST);
        List<Card> hostCards = cardDeckService.drawCards(deck, 8);
        hostCards = cardRepository.saveAll(hostCards);
        host.addCardsToHand(hostCards);
        host = playerRepository.save(host);
        
        Player guest = new Player(game.getGuest());
        guest.setRole(Role.GUEST);
        List<Card> guestCards = cardDeckService.drawCards(deck, 8);
        guestCards = cardRepository.saveAll(guestCards);
        guest.addCardsToHand(guestCards);
        guest = playerRepository.save(guest);

        // create the new round, add players and set starting turn
        Round round =  new Round();
        round.addPlayer(host);
        round.addPlayer(guest);
        round.setCurrentTurnPlayer(setStartingTurn(game));

        // add 4 cards to table, save 
        List<Card> tableCards = cardDeckService.drawCards(deck, 8);
        tableCards = cardRepository.saveAll(tableCards);
        round.addCardsToTable(tableCards);
        round = roundRepository.save(round);        

        // add the round to the game and update counter
        game.setCurrentRound(round);
        game.setTotalRounds(game.getTotalRounds()+1);
        game = gameRepository.save(game);

        // flush repositories
        gameRepository.flush();
        roundRepository.flush();
        playerRepository.flush();
        cardDeckRepository.flush();
        cardRepository.flush();

        // return the updated game
        return game;
    };


    private Role setStartingTurn(Game game) {
        if (game.getTotalRounds() % 2 == 1) {
            return game.getStartingPlayer();
        } else if (game.getStartingPlayer() == Role.HOST) {
            return Role.GUEST;
        } else {
            return Role.HOST;
        }
    }

    private Round executeMove(Round round, PlayerMoveMessage message) {
        // remove card from hand

        // remove cards from field

        // add card to field if move type correct

        return round;
    }
}
