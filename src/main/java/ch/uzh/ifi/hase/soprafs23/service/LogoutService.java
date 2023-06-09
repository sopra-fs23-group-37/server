package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Logout;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.LogoutRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class LogoutService {

  private final Logger log = LoggerFactory.getLogger(LogoutService.class);

  private final UserRepository userRepository;
  private final LogoutRepository logoutRepository;

  public LogoutService(@Qualifier("userRepository") UserRepository userRepository,
      @Qualifier("logoutRepository") LogoutRepository logoutRepository) {
    this.userRepository = userRepository;
    this.logoutRepository = logoutRepository;
  }

  public void createLogout(Long userId) {
    Logout newLogout = new Logout();
    newLogout.setLogoutDate(new Date());
    newLogout.setUserId(userId);
    Optional<User> optionalUser = userRepository.findById(userId);
    String baseErrorMessage = "User with id %x was not found";
    User user = optionalUser.orElseThrow(
        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(baseErrorMessage, userId)));

    if (user.getUserStatus() == UserStatus.OFFLINE) {
      String errorMessage = "Logout failed, because the user was not logged in";
      throw new ResponseStatusException(HttpStatus.CONFLICT, errorMessage);
    }

    user.setUserStatus(UserStatus.OFFLINE);

    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newLogout = logoutRepository.save(newLogout);
    logoutRepository.flush();

    userRepository.save(user);
    userRepository.flush();

    log.debug("Created Information for Logout: {}", newLogout);
  }

}
