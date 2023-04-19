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
import org.mockito.Mockito;
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

    private User testUser;

    private User newUser;

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

        newUser = new User();
        newUser.setUsername("johndoe");
        newUser.setPassword("password123");
        newUser.setUserStatus(UserStatus.OFFLINE);

        testLogin = new Login();
        testLogin.setUsername("testUsername");
        testLogin.setPassword("testPassword");

        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);
        Mockito.when(userRepository.save(newUser)).thenReturn(newUser);
        Mockito.when(loginRepository.save(testLogin)).thenReturn(testLogin);

        // save the users in the database
        testUser = userRepository.save(testUser);
        newUser = userRepository.save(newUser);
    }


    @Test
    public void createLogin_validInput_loginCreated() {
        // given
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);
        Login createdLogin = null;

        try {
            // when
            createdLogin = loginService.createLogin(testLogin);

        } catch (ResponseStatusException ex) {
            // then
            fail("Exception thrown: " + ex.getMessage());
        }

        // then
        assertNotNull(createdLogin);
        assertEquals(testUser.getUsername(), createdLogin.getUsername());
        assertEquals(testUser.getPassword(), createdLogin.getPassword());
    }

    @Test
    void createLogin_userNotFound_throwException() {
        // given
        String username = "nonExistingUser";
        String password = "password123";

        // when
        when(userRepository.findByUsername(username)).thenReturn(null);

        // then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            Login newLogin = new Login();
            newLogin.setUsername(username);
            newLogin.setPassword(password);
            loginService.createLogin(newLogin);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("The username does not exist!", exception.getReason());
        verify(userRepository, times(1)).findByUsername(username);
        verifyNoInteractions(loginRepository);
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

    @Test
    void testCreateLoginWithIncorrectPassword() {
        // given
        String username = "testUsername";
        String password = "wrongPassword";

        User existingUser = new User();
        existingUser.setUsername(username);
        existingUser.setPassword("testPassword");

        when(userRepository.findByUsername(username)).thenReturn(existingUser);

        // when
        Login newLogin = new Login();
        newLogin.setUsername(username);
        newLogin.setPassword(password);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            loginService.createLogin(newLogin);
        });

        // then
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("The password is incorrect!", exception.getReason());
        verify(userRepository, times(1)).findByUsername(username);
        verifyNoInteractions(loginRepository);
    }



}
