package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerStatus;

public class WSGameStatusDTO {

    // id
    private Long gameId;

    // participants
    private Long hostId;
    private String hostUsername;
    private Long guestId;
    private String guestUsername;

    // status
    private GameStatus gameStatus;
    private PlayerStatus hostStatus;
    private PlayerStatus guestStatus;

    // mode
    private Boolean isPrivate;
    private Boolean isSingleRound;

    // points and winner
    private int guestPoints;
    private int hostPoints;
    private Long winnerId;
    private String winnerUsername;

    // reason for the end of the game
    private String endGameReason;

    // code
    private String gameCode;

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getEndGameReason() {
        return endGameReason;
    }

    public void setEndGameReason(String endOfGameReason) {
        this.endGameReason = endOfGameReason;
    }

    public int getGuestPoints() {
        return guestPoints;
    }

    public void setGuestPoints(int guestPoints) {
        this.guestPoints = guestPoints;
    }

    public int getHostPoints() {
        return hostPoints;
    }

    public void setHostPoints(int hostPoints) {
        this.hostPoints = hostPoints;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getHostUsername() {
        return hostUsername;
    }

    public void setHostUsername(String hostUsername) {
        this.hostUsername = hostUsername;
    }

    public String getGuestUsername() {
        return guestUsername;
    }

    public void setGuestUsername(String guestUsername) {
        this.guestUsername = guestUsername;
    }

    public Long getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Long winnerId) {
        this.winnerId = winnerId;
    }

    public String getWinnerUsername() {
        return winnerUsername;
    }

    public void setWinnerUsername(String winnerUsername) {
        this.winnerUsername = winnerUsername;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long host) {
        this.hostId = host;
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guest) {
        this.guestId = guest;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

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

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Boolean getIsSingleRound() {
        return isSingleRound;
    }

    public void setIsSingleRound(Boolean isSingleRound) {
        this.isSingleRound = isSingleRound;
    }

}
