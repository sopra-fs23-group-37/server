package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class WSStatsDTO {

    private Long gamesPlayed;
    private Long gamesWon;

    public Long getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(Long gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public Long getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(Long gamesWon) {
        this.gamesWon = gamesWon;
    }

}
