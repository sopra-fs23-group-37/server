package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Login;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.LoginRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    private Login testLogin;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setUserId(1L);
        testUser.setToken("testToken");
        testUser.setUserStatus(UserStatus.OFFLINE);

        testLogin = new Login();
        testLogin.setUsername("testUsername");
        testLogin.setPassword("testPassword");
    }

    public void createLogin_validInput_loginCreated() {
        // create a new user
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password");

        // save the user to the repository
        User createdUser = userService.createUser(newUser);

        // create a new login
        Login newLogin = new Login();
        newLogin.setUsername(createdUser.getUsername());
        newLogin.setPassword(createdUser.getPassword());

        // create the login
        Login createdLogin = loginService.createLogin(newLogin);

        // check that the login was created successfully
        assertNotNull(createdLogin, "Login was not created successfully");
    }

    @Test
    public void createLogin_userNotFound_throwException() {
        // given
        when(userRepository.findByUsername(any())).thenReturn(null);

        // when and then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> loginService.createLogin(testLogin));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("The username does not exist!", exception.getReason());
        verify(loginRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void checkPassword_validInput_passwordMatches() {
        // given
        when(userRepository.findByUsername(any())).thenReturn(testUser);

        // when
        Boolean result = loginService.checkPassword(testLogin);

        // then
        assertTrue(result);
        verify(userRepository, times(1)).findByUsername(any());
    }

    @Test
    public void checkPassword_userNotFound_throwException() {
        // given
        when(userRepository.findByUsername(any())).thenReturn(null);

        // when and then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> loginService.checkPassword(testLogin));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("The username does not exist!", exception.getReason());
        verify(userRepository, times(1)).findByUsername(any());
    }
}

