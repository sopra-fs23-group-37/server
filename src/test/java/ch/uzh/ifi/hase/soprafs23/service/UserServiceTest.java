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

        testUser2 = new User();
        testUser2.setPassword("testPassword2");
        testUser2.setUsername("testUsername2");
        testUser2.setUserId(2L);
        testUser2.setToken("2");
        testUser2.setUserStatus(UserStatus.ONLINE);
        testUser2.setCreation_date(new Date());

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test 
    public void updateStatistics_winner() {
        User returnUser = userService.updateUserWinStatistics(testUser, true);

        assertEquals(1, returnUser.getGamesPlayed());
        assertEquals(1, returnUser.getGamesWon());
    }

    @Test 
    public void updateStatistics_loser() {
        User returnUser = userService.updateUserWinStatistics(testUser, false);

        assertEquals(1, returnUser.getGamesPlayed());
        assertEquals(0, returnUser.getGamesWon());
    }

    @Test
    public void createUser_validInputs_success() {
        User createdUser = userService.createUser(testUser);

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testUser.getUserId(), createdUser.getUserId());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.ONLINE, createdUser.getUserStatus());
    }

    @Test
    public void createUser_duplicateUsername_throwsException() {
        userService.createUser(testUser);

        Mockito.when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);

        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }

    @Test
    public void getUsers_returnsAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(testUser);
        userList.add(testUser2);

        Mockito.when(userRepository.findAll()).thenReturn(userList);

        List<User> returnedList = userService.getUsers();

        assertEquals(userList.size(), returnedList.size());
        assertTrue(returnedList.contains(testUser));
        assertTrue(returnedList.contains(testUser2));
    }

    @Test
    public void getUserById_validId_returnsUser() {
        Mockito.when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

        User returnedUser = userService.getUserById(testUser.getUserId());

        assertEquals(testUser, returnedUser);
    }

    @Test
    public void getUserById_invalidId_throwsException() {
        Mockito.when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.getUserById(testUser.getUserId()));
    }

    @Test
    public void deleteUser_validInputs_success() {
        // given -> a user with the given ID exists in the repository
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testUser));

        // when -> the user is deleted
        userService.deleteUser(testUser.getUserId());

        // then -> verify that the user was deleted successfully
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    public void deleteUser_invalidUserId_throwsException() {
        // given -> a user with the given ID does not exist in the repository
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // then -> attempt to delete the user -> check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.deleteUser(1L));
    }

    @Test
    public void getUserById_validInputs_returnsUser() {
        // given -> a user with the given ID exists in the repository
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testUser));

        // when -> the user is retrieved by ID
        User retrievedUser = userService.getUserById(testUser.getUserId());

        // then -> verify that the correct user was retrieved
        assertEquals(testUser.getUserId(), retrievedUser.getUserId());
        assertEquals(testUser.getUsername(), retrievedUser.getUsername());
    }

    @Test
    public void getUserById_invalidUserId_throwsException() {
        // given -> a user with the given ID does not exist in the repository
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // then -> attempt to retrieve the user by ID -> check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void getUserByUsername_validInputs_returnsUser() {
        // given -> a user with the given username exists in the repository
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(testUser);

        // when -> the user is retrieved by username
        User retrievedUser = userRepository.findByUsername(testUser.getUsername());

        // then -> verify that the correct user was retrieved
        assertEquals(testUser.getUserId(), retrievedUser.getUserId());
        assertEquals(testUser.getUsername(), retrievedUser.getUsername());
    }

}