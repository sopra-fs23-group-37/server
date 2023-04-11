package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;

import java.util.Date;

public class GameGetDTO {

    private Long gameId;

    private User host;

    private User guest;

    private User winner;

    private Date createdDate;

    private GameStatus gameStatus;

    private PlayerStatus hostStatus;

    private PlayerStatus guestStatus;



    // getters & setters 
    
    public PlayerStatus getHostStatus() {
        return hostStatus;
    }

    public void setHostStatus(PlayerStatus hostStatus) {
        this.hostStatus = hostStatus;
    }

    public PlayerStatus getGuestStatus() {
        return guestStatus;
    }

    public void setGuestStatus(PlayerStatus guestStatus) {
        this.guestStatus = guestStatus;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public User getGuest() {
        return guest;
    }

    public void setGuest(User guest) {
        this.guest = guest;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}