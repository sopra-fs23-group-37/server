package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerMoveMessage;

@SpringBootTest
public class MoveLogicServiceTest {
    private List<Card> mockCards;
    private Card mockCard6H;
    private Card mockCard8S;
    private Card mockCard8H;
    private Card mockCard2H;
    private Card mockCardKH;
    private Card mockCardJS;
    private PlayerMoveMessage mockPlayerMoveMessage;

    @InjectMocks
    private MoveLogicService moveLogicService;

    @BeforeEach
    public void setup() {
        mockPlayerMoveMessage = new PlayerMoveMessage();
        mockPlayerMoveMessage.setPlayerId(1);
        mockCards = new ArrayList<Card>();

        mockCard6H = new Card();
        mockCard6H.setCode("6H");
        mockCard6H.setValue("6");

        mockCard8S = new Card();
        mockCard8S.setCode("8S");
        mockCard8S.setValue("8");

        mockCard8H = new Card();
        mockCard8H.setCode("8H");
        mockCard8H.setValue("8");

        mockCard2H = new Card();
        mockCard2H.setCode("2H");
        mockCard2H.setValue("2");

        mockCardKH = new Card();
        mockCardKH.setCode("KH");
        mockCardKH.setValue("KING");

        mockCardJS = new Card();
        mockCardJS.setCode("JS");
        mockCardJS.setValue("JACK");
    }

    @Test
    public void checkMove1_success() {
        mockPlayerMoveMessage.setCardFromHand(mockCard8S);
        mockCards.add(mockCard8H);
        mockPlayerMoveMessage.setCardsFromField(mockCards);
        mockPlayerMoveMessage.setMoveType(1);

        assertEquals(true, moveLogicService.checkMove1(mockPlayerMoveMessage));
    }

    @Test 
    public void checkMove1_illegalMove() {
        mockPlayerMoveMessage.setCardFromHand(mockCard6H);
        mockCards.add(mockCard8H);
        mockPlayerMoveMessage.setCardsFromField(mockCards);
        mockPlayerMoveMessage.setMoveType(1);

        assertEquals(false, moveLogicService.checkMove1(mockPlayerMoveMessage));
    }

    @Test 
    public void checkMove1_wrongMoveTypeIn1() {
        mockPlayerMoveMessage.setCardFromHand(mockCard6H);
        mockCards.add(mockCard8H);
        mockCards.add(mockCard8S);
        mockPlayerMoveMessage.setCardsFromField(mockCards);
        mockPlayerMoveMessage.setMoveType(1);

        assertEquals(false, moveLogicService.checkMove1(mockPlayerMoveMessage));
    }

    @Test
    public void checkMove2_success() {
        mockPlayerMoveMessage.setCardFromHand(mockCard8S);
        mockCards.add(mockCard2H);
        mockCards.add(mockCard6H);
        mockPlayerMoveMessage.setCardsFromField(mockCards);
        mockPlayerMoveMessage.setMoveType(2);

        assertEquals(true, moveLogicService.checkMove2(mockPlayerMoveMessage));
    }

    @Test
    public void checkMove2_illegalMove() {
        mockPlayerMoveMessage.setCardFromHand(mockCard6H);
        mockCards.add(mockCard2H);
        mockCards.add(mockCard8H);
        mockPlayerMoveMessage.setCardsFromField(mockCards);
        mockPlayerMoveMessage.setMoveType(2);

        assertEquals(false, moveLogicService.checkMove2(mockPlayerMoveMessage));
    }

    @Test
    public void checkMove2_wrongMoveTypeIn2() {
        mockPlayerMoveMessage.setCardFromHand(mockCard6H);
        mockCards.add(mockCard2H);
        mockPlayerMoveMessage.setCardsFromField(mockCards);
        mockPlayerMoveMessage.setMoveType(2);

        assertEquals(false, moveLogicService.checkMove2(mockPlayerMoveMessage));
    }

    @Test
    public void checkMove3_success() {
        mockPlayerMoveMessage.setCardFromHand(mockCardJS);
        mockCards.add(mockCard2H);
        mockPlayerMoveMessage.setCardsFromField(mockCards);
        mockPlayerMoveMessage.setMoveType(3);

        assertEquals(true, moveLogicService.checkMove3(mockPlayerMoveMessage));
    }

    @Test
    public void checkMove3_wrongMoveTypeIn3() {
        mockPlayerMoveMessage.setCardFromHand(mockCardKH);
        mockCards.add(mockCard2H);
        mockPlayerMoveMessage.setCardsFromField(mockCards);
        mockPlayerMoveMessage.setMoveType(3);

        assertEquals(false, moveLogicService.checkMove3(mockPlayerMoveMessage));
    }

    @Test 
    public void checkMove4_success() {
        mockPlayerMoveMessage.setCardFromHand(mockCard2H);
        mockCards.add(mockCard6H);
        mockPlayerMoveMessage.setCardsFromField(mockCards);
        mockPlayerMoveMessage.setMoveType(4);

        assertEquals(true, moveLogicService.checkMove4(mockPlayerMoveMessage));
    }
}
