package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Login;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.LoginRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

/**
 * Login Service
 * This class is the "worker" and responsible for all functionality related to
 * the login
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class LoginService {

  private final Logger log = LoggerFactory.getLogger(LoginService.class);

  private final UserRepository userRepository;
  private final LoginRepository loginRepository;

  @Autowired
  public LoginService(@Qualifier("userRepository") UserRepository userRepository, @Qualifier("loginRepository") LoginRepository loginRepository) {
    this.userRepository = userRepository;
    this.loginRepository = loginRepository;
  }


  public Login createLogin(Login newLogin) {

    newLogin.setLoginDate(new Date());
    newLogin.setSuccessful(checkPassword(newLogin));
    User foundUser = userRepository.findByUsername(newLogin.getUsername());

    newLogin.setUserId(foundUser.getUserId());
    foundUser.setUserStatus(UserStatus.ONLINE);
    newLogin.setToken(foundUser.getToken());
    

    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newLogin = loginRepository.save(newLogin);
    loginRepository.flush();

    userRepository.save(foundUser);
    userRepository.flush();

    log.debug("Created Information for Login: {}", newLogin);
    return newLogin;
  }

    public Boolean checkPassword(Login newLogin) {
      User user = userRepository.findByUsername(newLogin.getUsername());
      if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The username does not exist!");
        }
      String correctPassword = user.getPassword();
      String enteredPassword = newLogin.getPassword();
      if (correctPassword.equals(enteredPassword)) {
          return true;
      } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The password is incorrect!");

    }


}
