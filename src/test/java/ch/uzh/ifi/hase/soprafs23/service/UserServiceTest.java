package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;
  private User testUser2;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

      testUser = new User();
      testUser.setPassword("testPassword");
      testUser.setUsername("testUsername");
      testUser.setUserId(1L);
      testUser.setToken("1");
      testUser.setUserStatus(UserStatus.ONLINE);
      testUser.setCreation_date(new Date());

      User testUser2 = new User();
      testUser2.setPassword("testPassword2");
      testUser2.setUsername("testUsername2");
      testUser2.setUserId(2L);
      testUser2.setToken("2");
      testUser2.setUserStatus(UserStatus.ONLINE);
      testUser2.setCreation_date(new Date());

    // when -> any object is being saved in the userRepository -> return the dummy
    // testUser
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
  }

  @Test
  public void createUser_validInputs_success() {
    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    User createdUser = userService.createUser(testUser);

    // then
    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

    assertEquals(testUser.getUserId(), createdUser.getUserId());
    //assertEquals(testUser.getName(), createdUser.getName());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertNotNull(createdUser.getToken());
    assertEquals(UserStatus.ONLINE, createdUser.getUserStatus());
  }

  // @Test
  // public void createUser_duplicateName_throwsException() {
  //   // given -> a first user has already been created
  //   userService.createUser(testUser);

  //   // when -> setup additional mocks for UserRepository
  //   //Mockito.when(userRepository.findByName(Mockito.any())).thenReturn(testUser);
  //   Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

  //   // then -> attempt to create second user with same user -> check that an error
  //   // is thrown
  //   assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  // }


    @Test
    public void createUser_duplicateUsername_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        //Mockito.when(userRepository.findByPassword(Mockito.any())).thenReturn(testUser);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }

    @Test
    public void returnAllUsersTest() {
        List<User> userList = new ArrayList<>();
        userList.add(testUser);
        userList.add(testUser2);

        Mockito.when(userRepository.findAll()).thenReturn(userList);

        List<User> returnedList = userService.getUsers();
        assertEquals(userList.size(), returnedList.size());
    }

}
