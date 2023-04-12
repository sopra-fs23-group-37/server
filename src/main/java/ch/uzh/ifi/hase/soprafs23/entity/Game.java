package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gameId;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "hostId")
    private User host;

    @ManyToOne
    @JoinColumn(name = "guestId")
    private User guest;

    @Enumerated(EnumType.STRING)
    private PlayerStatus hostStatus;

    @Enumerated(EnumType.STRING)
    private PlayerStatus guestStatus;

    @ManyToOne
    @JoinColumn(name = "winnerId")
    private User winner;

    @Column(nullable = false)
    private Date createdDate = new Date();

    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    @Enumerated(EnumType.STRING)
    private Role startingPlayer;

    @OneToOne(targetEntity = Round.class)
    private Round currentRound;

    private int totalRounds;

    private int hostPoints;

    private int guestPoints;

    public int getHostPoints() {
        return hostPoints;
    }

    public void setHostPoints(int hostPoints) {
        this.hostPoints = hostPoints;
    }

    public int getGuestPoints() {
        return guestPoints;
    }

    public void setGuestPoints(int guestPoints) {
        this.guestPoints = guestPoints;
    }

    public Role getStartingPlayer() {
        return startingPlayer;
    }

    public void setStartingPlayer(Role startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public void setTotalRounds(int totalRounds) {
        this.totalRounds = totalRounds;
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