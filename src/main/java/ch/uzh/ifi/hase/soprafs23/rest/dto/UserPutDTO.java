package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.Date;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

public class UserPutDTO {

  private Long userId;
  private String username;
  private String password;
  private UserStatus userStatus;
  private Date creation_date;
  private Date birthday;

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
