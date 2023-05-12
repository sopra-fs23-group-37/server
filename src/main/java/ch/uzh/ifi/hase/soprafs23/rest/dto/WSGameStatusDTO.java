package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerStatus;

public class WSGameStatusDTO {

    // id
    private Long gameId;

    // participants
    private Long hostId;
    private String hostUsername;
    private String hostAvatarUrl;
    private Long guestId;
    private String guestUsername;
    private String guestAvatarUrl;

    // status
    private GameStatus gameStatus;
    private PlayerStatus hostStatus;
    private PlayerStatus guestStatus;

    // points and winner
    private int guestPoints;
    private int hostPoints;
    private Long winnerId;
    private String winnerUsername;

    // reason for the end of the game
    private String endGameReason;

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

    public String getHostAvatarUrl() { return hostAvatarUrl; }

    public void setHostAvatarUrl(String hostAvatar) { this.hostAvatarUrl = hostAvatarUrl; }

    public String getGuestUsername() {
        return guestUsername;
    }

    public void setGuestUsername(String guestUsername) {
        this.guestUsername = guestUsername;
    }

    public String getGuestAvatarUrl() { return guestAvatarUrl; }

    public void setGuestAvatarUrl(String guestAvatarUrl) { this.guestAvatarUrl = guestAvatarUrl; }

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
}
