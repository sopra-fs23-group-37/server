package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.constant.RoundStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.CardDeck;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Round;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoundRepository;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerMoveMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RoundService {

    private final RoundRepository roundRepository;
    private final PlayerRepository playerRepository;

    private final CardDeckService cardDeckService;

    Logger logger = LoggerFactory.getLogger(RoundService.class);

    RoundService(
            @Qualifier("roundRepository") RoundRepository roundRepository,
            @Qualifier("playerRepository") PlayerRepository playerRepository,
            CardDeckService cardDeckService) {
        this.roundRepository = roundRepository;
        this.playerRepository = playerRepository;
        this.cardDeckService = cardDeckService;
    }

    public Round newRound(Game game) throws IOException, InterruptedException {

        logger.info(String.format("Starting a new round for game %d", game.getGameId()));

        // create the new round
        Round round = new Round();
        round.setRoundStatus(RoundStatus.ONGOING);

        // add a shuffled deck to the round
        CardDeck deck = cardDeckService.createDeck();
        round.setCardDeck(deck);

        // create player hands, deal 8 cards to each
        Player host = new Player(game.getHost(), Role.HOST);
        host = dealEightCards(host, deck);

        Player guest = new Player(game.getGuest(), Role.GUEST);
        guest = dealEightCards(guest, deck);

        // add players to round and set starting turn
        round.setHost(host);
        round.setGuest(guest);
        round.setCurrentTurnPlayer(determineStartTurn(game));

        // add 4 cards to table, save
        round = dealFourCardsTable(round, deck);
        round = roundRepository.save(round);
        roundRepository.flush();

        // return the new round
        return round;
    };

    public boolean postMoveChecks(Round round) throws IOException, InterruptedException {

        // check if both player hands are empty
        if (round.getHost().getCardsInHand().isEmpty()
                && round.getGuest().getCardsInHand().isEmpty()) {

            // check if deck is empty
            if (round.getCardDeck().getRemaining() == 0) {

                // end the round
                round = endRound(round);

                // return end of round = true
                return true;
            } else {
                // deal new cards to players
                Player host = dealEightCards(round.getHost(), round.getCardDeck());
                Player guest = dealEightCards(round.getGuest(), round.getCardDeck());
                round.setHost(host);
                round.setGuest(guest);
                round = roundRepository.save(round);
                roundRepository.flush();
            }
        }
        // return end of round = false
        return false;
    }

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

    public Player dealEightCards(Player player, CardDeck deck) throws IOException, InterruptedException {
        logger.info(String.format("Dealing 8 cards to player %s ", player.getPlayer().getUsername()));

        List<Card> cards = cardDeckService.drawCards(deck, 8);
        player.addCardsToHand(cards);
        player = playerRepository.save(player);
        playerRepository.flush();
        return player;
    }

    public Round dealFourCardsTable(Round round, CardDeck deck) throws IOException, InterruptedException {
        logger.info("Adding 4 cards to the table");

        List<Card> tableCards = cardDeckService.drawCards(deck, 4);
        round.addCardsToTable(tableCards);
        return round;
    }

    public Round endRound(Round round) {
        logger.info("Ending the round");

        // give any remaining table cards to the player who last grabbed some cards
        Player recipient = round.getLastCardGrab() == Role.HOST ? round.getHost() : round.getGuest();
        for (Card c : round.getTableCards()) {
            recipient.addCardToDiscard(c);
        }

        round.removeAllCardsFromTable();

        // count points of the hands
        round.setHostPoints(round.getHost().countDiscard());
        round.setGuestPoints(round.getGuest().countDiscard());

        // set Round status
        round.setRoundStatus(RoundStatus.FINISHED);

        // return the Round
        return round;
    }

    public Round executeMove(Round round, PlayerMoveMessage message) {
        // check if move is made by correct player
        Player player = round.getCurrentTurnPlayer() == Role.HOST ? round.getHost() : round.getGuest();

        // add a list of captured cards to keep track
        List<Card> capturedCards = new ArrayList<>();

        // we need a structure to tell if a move was successful or not
        if (player.getPlayer().getUserId() != message.getPlayerId()) {
            return round;
        }

        if (message.getMoveType() != 4) {
            // remove card from hand
            player.removeCardFromHand(message.getCardFromHand());
            player.addCardToDiscard(message.getCardFromHand());
            capturedCards.add(message.getCardFromHand());

            // remove cards from field and add it to that players discard
            for (Card c : message.getCardsFromField()) {
                round.removeCardFromTable(c);
                player.addCardToDiscard(c);
                capturedCards.add(c);
            }
            round.setLastCardGrab(player.getRole());

            // overwrite the last captured cards on the player
            player.setLastCapturedCards(capturedCards);

        } else {
            // add card to field if move type correct
            player.removeCardFromHand(message.getCardFromHand());
            round.addCardToTable(message.getCardFromHand());
        }

        // change player turn if the other player still has cards
        if (round.getCurrentTurnPlayer().equals(Role.GUEST)) {
            round.setCurrentTurnPlayer(Role.HOST);

        } else if (round.getCurrentTurnPlayer().equals(Role.HOST)) {
            round.setCurrentTurnPlayer(Role.GUEST);
        }

        round = roundRepository.save(round);

        return round;
    }
}
