package ch.uzh.ifi.hase.soprafs23.rest.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs23.constant.RoundStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Card;

public class WSRoundStatusDTOTest {

    @Test
    public void testGettersAndSetters() {
        WSRoundStatusDTO dto = new WSRoundStatusDTO();

        // Set values for all fields
        dto.setRoundStatus(RoundStatus.ONGOING);
        dto.setMyPointsTotalCards(10);
        dto.setMyPointClubs(2);
        dto.setMyTwoOfClubs(1);
        dto.setMyTenOfDiamonds(0);
        dto.setMyTotalPoints(5);
        dto.setOppPointsTotalCards(5);
        dto.setOppPointClubs(1);
        dto.setOppTwoOfClubs(0);
        dto.setOppTenOfDiamonds(1);
        dto.setOppTotalPoints(2);
        List<Card> myCardsInHand = new ArrayList<>();
        Card aceSpades = new Card();
        aceSpades.setCode("AS");
        myCardsInHand.add(aceSpades);
        dto.setMyCardsInHand(myCardsInHand);
        List<Card> myCardsInDiscard = new ArrayList<>();
        myCardsInDiscard.add(new Card());
        dto.setMyCardsInDiscard(myCardsInDiscard);
        dto.setOppCards(3);
        List<Card> oppCardsInDiscard = new ArrayList<>();
        oppCardsInDiscard.add(new Card());
        dto.setOppCardsInDiscard(oppCardsInDiscard);
        List<Card> cardsOnTable = new ArrayList<>();
        cardsOnTable.add(new Card());
        dto.setCardsOnTable(cardsOnTable);
        dto.setDeckCards(true);
        dto.setMyTurn(true);
        dto.setOpponentLeft(false);
        dto.setOpponentLeftReason(null);

        // Verify that the values were set correctly
        Assertions.assertEquals(RoundStatus.ONGOING, dto.getRoundStatus());
        Assertions.assertEquals(10, dto.getMyPointsTotalCards());
        Assertions.assertEquals(2, dto.getMyPointClubs());
        Assertions.assertEquals(1, dto.getMyTwoOfClubs());
        Assertions.assertEquals(0, dto.getMyTenOfDiamonds());
        Assertions.assertEquals(5, dto.getMyTotalPoints());
        Assertions.assertEquals(5, dto.getOppPointsTotalCards());
        Assertions.assertEquals(1, dto.getOppPointClubs());
        Assertions.assertEquals(0, dto.getOppTwoOfClubs());
        Assertions.assertEquals(1, dto.getOppTenOfDiamonds());
        Assertions.assertEquals(2, dto.getOppTotalPoints());
        Assertions.assertEquals(myCardsInHand, dto.getMyCardsInHand());
        Assertions.assertEquals(myCardsInDiscard, dto.getMyCardsInDiscard());
        Assertions.assertEquals(3, dto.getOppCards());
        Assertions.assertEquals(oppCardsInDiscard, dto.getOppCardsInDiscard());
        Assertions.assertEquals(cardsOnTable, dto.getCardsOnTable());
        Assertions.assertTrue(dto.getDeckCards());
        Assertions.assertTrue(dto.getMyTurn());
        Assertions.assertFalse(dto.getOpponentLeft());
        Assertions.assertNull(dto.getOpponentLeftReason());

    }

}
