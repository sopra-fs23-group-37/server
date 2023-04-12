/*
package ch.uzh.ifi.hase.soprafs23.service;

import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

import ch.uzh.ifi.hase.soprafs23.entity.Login;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.LoginRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import static org.mockito.Mockito.when;

 TODO Implement Database and then use tests
@SpringBootTest
public class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoginRepository loginRepository;

    @InjectMocks
    private LoginService loginService;

    @InjectMocks
    private UserService userService;


    private User testUser;

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

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test
    public void testCreateLogin() {

        User createdUser = userService.createUser(testUser);

        // create a new login
        Login newLogin = new Login();
        newLogin.setUsername("testUsername");
        newLogin.setPassword("testPassword");

        // mock the userRepository's findByUsername() method to return a user
        User foundUser = new User();
        foundUser.setUsername("testUsername");
        foundUser.setPassword("testPassword");
        foundUser.setUserId(1L);
        foundUser.setUserStatus(UserStatus.OFFLINE);
        foundUser.setToken("testToken");
        when(userRepository.findByUsername("testUsername")).thenReturn(foundUser);

        // mock the loginRepository's save() method to return the same login object
        when(loginRepository.save(newLogin)).thenReturn(newLogin);

        // test the createLogin() method
        Login createdLogin = loginService.createLogin(newLogin);

        // verify that the createLogin() method returns the correct login object
        assertEquals(newLogin, createdLogin);

        // verify that the user's status was changed to ONLINE
        assertEquals(UserStatus.ONLINE, foundUser.getUserStatus());

        // verify that the token was set on the login object
        assertEquals("testtoken", newLogin.getToken());
    }

    @Test
    public void testCreateLoginWithInvalidUsername() {
        // create a new login with an invalid username
        Login newLogin = new Login();
        newLogin.setUsername("invaliduser");
        newLogin.setPassword("testpassword");

        // mock the userRepository's findByUsername() method to return null
        when(userRepository.findByUsername("invaliduser")).thenReturn(null);

        // test that the createLogin() method throws a ResponseStatusException with status NOT_FOUND
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            loginService.createLogin(newLogin);
        });
        assertEquals(exception.getStatus(), org.springframework.http.HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCreateLoginWithIncorrectPassword() {
        // create a new user
        User newUser = new User();
        newUser.setUsername("johndoe");
        newUser.setPassword("password123");
        newUser.setBirthday(new Date(2000, 1, 1));

        // save the user to the UserRepository
        userRepository.save(newUser);
        userRepository.flush();

        // create a new login with incorrect password
        Login newLogin = new Login();
        newLogin.setUsername("johndoe");
        newLogin.setPassword("wrongpassword");

        // attempt to create the login
        try {
            loginService.createLogin(newLogin);
            fail("Expected ResponseStatusException was not thrown");
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus());
            assertEquals("The password is incorrect!", e.getReason());
        }

        // verify that the login was not created
        assertEquals(0, loginRepository.count());
    }
}

 */