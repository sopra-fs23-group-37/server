package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long userId;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private UserStatus userStatus;

  @Column(nullable = false)
  private Date creation_date;

  @Column(nullable = true)
  private Date birthday;

  @Column(nullable = false)
  private Long gamesPlayed = 0L;

  @Column(nullable = false)
  private Long gamesWon = 0L;

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

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public UserStatus getUserStatus() {
    return userStatus;
  }

  public void setUserStatus(UserStatus userStatus) {
    this.userStatus = userStatus;
  }

  public Date getCreation_date() {
    return creation_date;
  }

  public void setCreation_date(Date creation_date) {
    this.creation_date = creation_date;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

}
