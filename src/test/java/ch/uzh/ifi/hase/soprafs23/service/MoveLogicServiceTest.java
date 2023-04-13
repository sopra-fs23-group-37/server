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
    private Card mockCard1;
    private Card mockCard2;
    private Card mockCard3;
    private PlayerMoveMessage mockPlayerMoveMessage;

    @InjectMocks
    private MoveLogicService moveLogicService;

    @BeforeEach
    public void setup() {
        mockPlayerMoveMessage = new PlayerMoveMessage();
        mockPlayerMoveMessage.setPlayerId(1);
        mockCards = new ArrayList<Card>();

        mockCard1 = new Card();
        mockCard1.setCode("6H");
        mockCard1.setValue("6");

        mockCard2 = new Card();
        mockCard2.setCode("8S");
        mockCard2.setValue("8");

        mockCard3 = new Card();
        mockCard3.setCode("8H");
        mockCard3.setValue("8");
    }

    @Test
    public void checkMove1_success() {
        mockPlayerMoveMessage.setCardFromHand(mockCard2);
        mockCards.add(mockCard3);
        mockPlayerMoveMessage.setCardFromField(mockCards);
        mockPlayerMoveMessage.setMoveType(1);

        assertEquals(true, moveLogicService.checkMove1(mockPlayerMoveMessage));
    }

    @Test public void checkMove1_illegalMove() {
        mockPlayerMoveMessage.setCardFromHand(mockCard1);
        mockCards.add(mockCard3);
        mockPlayerMoveMessage.setCardFromField(mockCards);
        mockPlayerMoveMessage.setMoveType(1);

        assertEquals(false, moveLogicService.checkMove1(mockPlayerMoveMessage));
    }

    @Test public void checkMove1_wrongMoveTypeIn1() {
        mockPlayerMoveMessage.setCardFromHand(mockCard1);
        mockCards.add(mockCard3);
        mockCards.add(mockCard2);
        mockPlayerMoveMessage.setCardFromField(mockCards);
        mockPlayerMoveMessage.setMoveType(1);

        assertEquals(false, moveLogicService.checkMove1(mockPlayerMoveMessage));
    }
}
