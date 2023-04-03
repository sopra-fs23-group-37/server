package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Logout;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.LogoutRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LogoutServiceTest {

    @Autowired
    private LogoutService logoutService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private LogoutRepository logoutRepository;

    @Test
    public void createLogout_validUserIdAndLoggedInUser_logoutCreated() {
        // given
        Long userId = 1L;
        User loggedInUser = new User();
        loggedInUser.setUserId(userId);
        loggedInUser.setUserStatus(UserStatus.ONLINE);

        Logout createdLogout = new Logout();
        createdLogout.setId(1L);
        createdLogout.setUserId(userId);
        createdLogout.setLogoutDate(new Date());

        when(userRepository.findById(userId)).thenReturn(Optional.of(loggedInUser));
        when(logoutRepository.save(any(Logout.class))).thenReturn(createdLogout);

        // when
        logoutService.createLogout(userId);

        // then
        verify(userRepository, times(1)).save(loggedInUser);
        verify(logoutRepository, times(1)).save(any(Logout.class));
    }

    @Test
    public void createLogout_validUserIdAndNotLoggedInUser_throwsException() {
        // given
        Long userId = 1L;
        User notLoggedInUser = new User();
        notLoggedInUser.setUserId(userId);
        notLoggedInUser.setUserStatus(UserStatus.OFFLINE);

        when(userRepository.findById(userId)).thenReturn(Optional.of(notLoggedInUser));

        // when
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            logoutService.createLogout(userId);
        });

        // then
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    @Test
    public void createLogout_invalidUserId_throwsException() {
        // given
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            logoutService.createLogout(userId);
        });

        // then
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }
}
