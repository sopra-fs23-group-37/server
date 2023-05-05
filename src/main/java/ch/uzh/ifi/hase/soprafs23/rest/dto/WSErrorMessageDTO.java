package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.WSErrorType;

public class WSErrorMessageDTO {

    private String message;

    private WSErrorType type;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public WSErrorType getType() {
        return type;
    }

    public void setType(WSErrorType type) {
        this.type = type;
    }

}
