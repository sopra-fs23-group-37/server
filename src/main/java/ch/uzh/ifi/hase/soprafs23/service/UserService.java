package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  private final WebsocketService websocketService;

  public UserService(@Qualifier("userRepository") UserRepository userRepository, WebsocketService websocketService) {
    this.userRepository = userRepository;
    this.websocketService = websocketService;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User createUser(User newUser) {
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setUserStatus(UserStatus.ONLINE);
    newUser.setCreation_date(new Date());

    checkIfUserExists(newUser);

    checkIfUserIsValid(newUser);

    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
    }
  }

  private void checkIfUserIsValid(User userToBeCreated) {
    if (userToBeCreated.getUsername().isEmpty() && userToBeCreated.getPassword().isEmpty()) {
      String ErrorMessage = "Creating user failed because username and password are empty";
      throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage);
    }

    else if (userToBeCreated.getUsername().isEmpty()) {
      String ErrorMessage = "Creating a new user failed because username is empty";
      throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage);
    }

    else if (userToBeCreated.getPassword().isEmpty()) {
      String ErrorMessage = "Creating a new user failed because password is empty";
      throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage);
    }
  }

  public User getUserById(Long id) {
    Optional<User> optionalUserFound = this.userRepository.findById(id);

    // handle errors if no user with that id exists
    String baseErrorMessage = "User with id %x was not found";
    User user = optionalUserFound
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, id)));

    return user;
  }



    public void updateUser(Long userId, User userInput) {
    User updateUser = getUserById(userId);

    if (checkIfUsernameExistsWithoutMine(userId, userInput)) {
      String ErrorMessage = "Modifying the user failed because username already exists";
      throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage);

    } else if (updateUser.getUserStatus() == UserStatus.OFFLINE) {
      String ErrorMessage = "Modifying the user failed because user is offline";
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage);

    } else if (userInput.getUsername().isEmpty() || userInput.getUsername().equals("")) {
      String ErrorMessage = "Modifying the user failed because username is empty";
      throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage);

      // if the avatarUrl is not null or "", then give error!
    } else if (userInput.getAvatarUrl().isEmpty() || userInput.getAvatarUrl().equals("")) {
      String ErrorMessage = "Modifying the user failed because avatarUrl is empty";
      throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage);

    } else {
      updateUser.setUsername(userInput.getUsername());
      updateUser.setBirthday(userInput.getBirthday());
      updateUser.setAvatarUrl(userInput.getAvatarUrl());

      userRepository.save(updateUser);
      userRepository.flush();
    }
  }

  private boolean checkIfUsernameExistsWithoutMine(Long userId, User userToBeCreated) {

    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    if (userRepository.findByUserId(userId) == userByUsername) {
      return false;
    } else {
      return (userByUsername != null);
    }
  }

  public void deleteUser(Long userId) {
    // check if user exists in repository
    User userToDelete = userRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    // delete user
    userRepository.deleteById(userToDelete.getUserId());
  }

  public User updateUserWinStatistics(User user, Boolean winner) {
    user.setGamesPlayed(user.getGamesPlayed() + 1);
    if (winner) {
      user.setGamesWon(user.getGamesWon() + 1);
    }

    userRepository.save(user);
    userRepository.flush();

    websocketService.sendStatsUpdateToUser(user);

    return user;
  }

}
