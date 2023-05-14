package ch.uzh.ifi.hase.soprafs23.rest.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerStatus;

public class WSGameStatusDTOTest {

    @Test
    public void testGettersAndSetters() {
        WSGameStatusDTO dto = new WSGameStatusDTO();

        // Set values for all fields
        dto.setGameId(1L);
        dto.setHostId(2L);
        dto.setHostUsername("alice");
        dto.setHostAvatarUrl("hostAvatarUrl");
        dto.setGuestId(3L);
        dto.setGuestUsername("bob");
        dto.setGuestAvatarUrl("guestAvatarUrl");
        dto.setGameStatus(GameStatus.ONGOING);
        dto.setHostStatus(PlayerStatus.WAITING);
        dto.setGuestStatus(PlayerStatus.CONNECTED);
        dto.setHostPoints(2);
        dto.setGuestPoints(1);
        dto.setWinnerId(2L);
        dto.setWinnerUsername("alice");

        // Verify that the values were set correctly
        Assertions.assertEquals(1L, dto.getGameId());
        Assertions.assertEquals(2L, dto.getHostId());
        Assertions.assertEquals("alice", dto.getHostUsername());
        Assertions.assertEquals("hostAvatarUrl", dto.getHostAvatarUrl());
        Assertions.assertEquals(3L, dto.getGuestId());
        Assertions.assertEquals("bob", dto.getGuestUsername());
        Assertions.assertEquals("guestAvatarUrl", dto.getGuestAvatarUrl());
        Assertions.assertEquals(GameStatus.ONGOING, dto.getGameStatus());
        Assertions.assertEquals(PlayerStatus.WAITING, dto.getHostStatus());
        Assertions.assertEquals(PlayerStatus.CONNECTED, dto.getGuestStatus());
        Assertions.assertEquals(2, dto.getHostPoints());
        Assertions.assertEquals(1, dto.getGuestPoints());
        Assertions.assertEquals(2L, dto.getWinnerId());
        Assertions.assertEquals("alice", dto.getWinnerUsername());

        // Modify some of the values and verify that they were changed correctly
        dto.setGameStatus(GameStatus.FINISHED);
        dto.setGuestStatus(PlayerStatus.WAITING);
        dto.setHostPoints(4);
        dto.setWinnerId(3L);
        dto.setWinnerUsername("charlie");

        Assertions.assertEquals(GameStatus.FINISHED, dto.getGameStatus());
        Assertions.assertEquals(PlayerStatus.WAITING, dto.getGuestStatus());
        Assertions.assertEquals(4, dto.getHostPoints());
        Assertions.assertEquals(3L, dto.getWinnerId());
        Assertions.assertEquals("charlie", dto.getWinnerUsername());
    }

}
