package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.Date;

public class LoginGetDTO {

  private Long id;
  private String username;
  private String password;
  private Date loginDate;
  private Boolean successful;
  private Long userId;
  private String token;


    public String getToken() {
    return token;
}

public void setToken(String token) {
    this.token = token;
}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
