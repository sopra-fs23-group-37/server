package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */

@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

  @Qualifier("userRepository")
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @BeforeEach
  public void setup() {
    userRepository.deleteAll();
  }

  // updated test from template
  @Test
  public void createUser_validInputs_success() {
    // given
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    //testUser.setName("testName");
    testUser.setUsername("testUsername");
    testUser.setUsername("testUsername");
    testUser.setPassword("password");
    testUser.setCreation_date(new Date());

    // when
    User createdUser = userService.createUser(testUser);

    // then
    assertEquals(testUser.getUserId(), createdUser.getUserId());
    //assertEquals(testUser.getName(), createdUser.getName());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertNotNull(createdUser.getToken());
    assertEquals(UserStatus.ONLINE, createdUser.getUserStatus());
  }

  // updated test from template
  @Test
  public void createUser_duplicateUsername_throwsException() {
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    //testUser.setName("testName");
    testUser.setUsername("testUsername");
    testUser.setPassword("password");
    testUser.setCreation_date(new Date());

    userService.createUser(testUser);

    // attempt to create second user with same username
    User testUser2 = new User();
    testUser2.setUsername("testUsername");

    // check that an error is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
  }

  //new test to check update of user
  @Test
  public void updateUser_validId_success() {
      assertNull(userRepository.findByUsername("testUsername"));

      User testUser = new User();
      testUser.setUsername("testUsername");
      testUser.setPassword("password");
      testUser.setCreation_date(new Date());
      userService.createUser(testUser);

      User createdUser = userRepository.findByUsername("testUsername");
      Long id = createdUser.getUserId();

      User updatedUser = new User();
      updatedUser.setUsername("updatedUsername");
      updatedUser.setBirthday(new Date(552218400000L));

      userService.updateUser(id, updatedUser);

      User finalUser = userService.getUserById(id);

      assertEquals(updatedUser.getUsername(), finalUser.getUsername());
      assertEquals(updatedUser.getBirthday(), finalUser.getBirthday());
  }

  // new test to check get user by id
    @Test
    public void getUser_validId_success() {
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("password");
        testUser.setCreation_date(new Date());
        userService.createUser(testUser);

        User createdUser = userRepository.findByUsername("testUsername");
        Long id = createdUser.getUserId();

        User foundUser = userService.getUserById(id);

        assertEquals(createdUser.getUsername(), foundUser.getUsername());
        assertEquals(createdUser.getPassword(), foundUser.getPassword());
        assertEquals(createdUser.getCreation_date(), foundUser.getCreation_date());
        assertEquals(createdUser.getBirthday(), foundUser.getBirthday());
    }

    // new test to check get user with invalid id
    @Test
    public void getUser_invalidId_throwsException() {
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("password");
        testUser.setCreation_date(new Date());
        testUser.setUserId(1L);
        userService.createUser(testUser);

        ResponseStatusException e =
                assertThrows(ResponseStatusException.class, () -> userService.getUserById(2L));
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        assertEquals("User with id 2 was not found", e.getReason());
    }

    // new test to check update user with invalid id
    @Test
    public void updateUser_invalidId_throwsException() {
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("password");
        testUser.setCreation_date(new Date());
        testUser.setUserId(1L);
        userService.createUser(testUser);

        User updatedUser = new User();
        updatedUser.setUsername("updatedUsername");
        updatedUser.setBirthday(new Date(552218400000L));


        ResponseStatusException e =
                assertThrows(ResponseStatusException.class, () -> userService.updateUser(2L, updatedUser));
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        assertEquals("User with id 2 was not found", e.getReason());
    }




}
