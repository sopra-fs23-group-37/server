package ch.uzh.ifi.hase.soprafs23.rest.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.constant.WSErrorType;

public class WSErrorMessageDTOTest {

    @Test
    public void testGettersAndSetters() {

        // create new object instance
        WSErrorMessageDTO dto = new WSErrorMessageDTO();

        // set all values
        dto.setMessage("Test Message");
        dto.setType(WSErrorType.INVALIDMOVE);

        // assert the getters return those values
        assertEquals("Test Message", dto.getMessage());
        assertEquals(WSErrorType.INVALIDMOVE, dto.getType());
    }

}
