package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.SessionStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SESSION")
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long sessionId;

    @ManyToOne
    @JoinColumn(name = "gameId")
    private Game game;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "hostId")
    private User host;

    @ManyToOne
    @JoinColumn(name = "guestId")
    private User guest;

    @ManyToMany
    private Set<User> spectators = new HashSet<>();

    @Column(nullable = false)
    private Date createdDate = new Date();

    @Enumerated(EnumType.STRING)
    private SessionStatus sessionStatus;

    @Column(nullable = true)
    private String identifier;

    @Column(nullable = false)
    private boolean isPrivate = false;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
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

    public Set<User> getSpectators() {
        return spectators;
    }

    public void setSpectators(Set<User> spectators) {
        this.spectators = spectators;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public SessionStatus getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(SessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

}
