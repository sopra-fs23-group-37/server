package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.constant.RoundStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.CardDeck;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Round;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs23.repository.RoundRepository;

public class RoundServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private RoundRepository roundRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private CardDeckService cardDeckService;

    @InjectMocks
    private RoundService roundService;

    private User testHost;
    private User testGuest;
    private Game testGame;
    private Player guestPlayer;
    private Player hostPlayer;
    private CardDeck testDeck;
    private List<Card> cards8 = new ArrayList<>();
    private List<Card> cards4 = new ArrayList<>();
    private Round testRound;

    @BeforeEach
    public void setup() throws IOException, InterruptedException {
        // initial setup so that test host, guest, and game are available to work with
        // from the repositories

        MockitoAnnotations.openMocks(this);

        testHost = new User();
        testHost.setUsername("testUsername");
        testHost.setUserId(1L);
        hostPlayer = new Player(testHost, Role.HOST);

        testGuest = new User();
        testGuest.setUsername("testUsername2");
        testGuest.setUserId(2L);
        guestPlayer = new Player(testGuest, Role.GUEST);

        testGame = new Game();
        testGame.setHost(testHost);
        testGame.setGuest(testGuest);
        testGame.setGameId(3L);
        testGame.setTotalRounds(0);
        testGame.setStartingPlayer(Role.HOST);

        testDeck = new CardDeck();
        testDeck.setCardDeckId(4L);

        Card card1 = new Card();
        Card card2 = new Card();
        Card card3 = new Card();
        Card card4 = new Card();
        Card card5 = new Card();
        Card card6 = new Card();
        Card card7 = new Card();
        Card card8 = new Card();

        cards8 = Arrays.asList(card1, card2, card3, card4, card5, card6, card7, card8);
        cards4 = Arrays.asList(card1, card2, card3, card4);

        testRound = new Round();
        testRound.setRoundId(5L);
        testRound.setCardDeck(testDeck);
        testRound.setCurrentTurnPlayer(Role.HOST);
        testRound.setGuest(guestPlayer);
        testRound.setHost(hostPlayer);

        Mockito.when(playerRepository.save(guestPlayer)).thenReturn(guestPlayer);
        Mockito.when(playerRepository.save(hostPlayer)).thenReturn(hostPlayer);
        Mockito.when(cardDeckService.createShuffledDeck()).thenReturn(testDeck);
        Mockito.when(cardDeckService.drawCards(testDeck, 8)).thenReturn(cards8);
        Mockito.when(cardDeckService.drawCards(testDeck, 4)).thenReturn(cards4);

    }

    @Test
    public void setStartingTurn_firstRound() {

        // given no round have been played yet and the game's starting player is the
        // host
        testGame.setStartingPlayer(Role.HOST);
        testGame.setTotalRounds(0);

        // get the starting player for the round
        Role returnedRole = roundService.determineStartTurn(testGame);

        // check that it's the host
        assertEquals(Role.HOST, returnedRole);
    }

    @Test
    public void setStartingTurn_secondRound_hostStarted() {

        // given no round have been played yet and the game's starting player is the
        // host
        testGame.setStartingPlayer(Role.HOST);
        testGame.setTotalRounds(1);

        // get the starting player for the round
        Role returnedRole = roundService.determineStartTurn(testGame);

        // check that it's the guest
        assertEquals(Role.GUEST, returnedRole);
    }

    @Test
    public void setStartingTurn_secondRound_guestStarted() {

        // given no round have been played yet and the game's starting player is the
        // host
        testGame.setStartingPlayer(Role.GUEST);
        testGame.setTotalRounds(1);

        // get the starting player for the round
        Role returnedRole = roundService.determineStartTurn(testGame);

        // check that it's the guest
        assertEquals(Role.HOST, returnedRole);
    }

    @Test
    public void deal8Cards_success() throws IOException, InterruptedException {
        roundService.dealEightCards(guestPlayer, testDeck);

        assertEquals(8, guestPlayer.getCardsInHand().size());
    }

    @Test
    public void deal4CardsTable_success() throws IOException, InterruptedException {
        roundService.dealFourCardsTable(testRound, testDeck);
        ;

        assertEquals(4, testRound.getTableCards().size());
    }

    @Test
    public void newRound_success() throws IOException, InterruptedException {
        // given
        Mockito.when(roundRepository.save(Mockito.any())).thenReturn(testRound);

        Round newRound = roundService.newRound(testGame);

        assertEquals(testRound.getHost(), newRound.getHost());
        assertEquals(testRound.getGuest(), newRound.getGuest());
        assertEquals(testRound.getCardDeck(), newRound.getCardDeck());

    }

    @Test
    public void endRound_hostGrabsRest() {
        testRound.setLastCardGrab(Role.HOST);
        // end the round
        Round endedRound = roundService.endRound(testRound);

        // check the status is updated. no points as neither testplayer has any cards
        assertEquals(RoundStatus.FINISHED, endedRound.getRoundStatus());
        assertEquals(0, endedRound.getHostPoints());
        assertEquals(0, endedRound.getGuestPoints());
    }

    @Test
    public void endRound_guestGrabsRest() {
        testRound.setLastCardGrab(Role.GUEST);
        // end the round
        Round endedRound = roundService.endRound(testRound);

        // check the status is updated. no points as neither testplayer has any cards
        assertEquals(RoundStatus.FINISHED, endedRound.getRoundStatus());
        assertEquals(0, endedRound.getHostPoints());
        assertEquals(0, endedRound.getGuestPoints());
    }

    @Test
    public void postMoveChecks_dealCards() throws IOException, InterruptedException {
        testDeck.setRemaining(32);

        boolean endOfRound = roundService.postMoveChecks(testRound);

        assertEquals(false, endOfRound);
    }

    @Test
    public void postMoveChecks_endRound() throws IOException, InterruptedException {
        testDeck.setRemaining(0);

        boolean endOfRound = roundService.postMoveChecks(testRound);

        assertEquals(true, endOfRound);
    }

}
