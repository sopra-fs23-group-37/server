/*package ch.uzh.ifi.hase.soprafs23.repository;
import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user = new User();

    @BeforeEach
    private void given(){
        user.setUsername("Max");
        user.setPassword("123");
        user.setUserStatus(UserStatus.ONLINE);
        user.setToken("1");
        user.setCreation_date(new Date());

        entityManager.persistAndFlush(user);
    }

    @Test
    public void findByUsername_success() {
        // when
        User found = userRepository.findByUsername(user.getUsername());

        // then
        assertNotNull(found.getUserId());
        assertEquals(found.getPassword(), user.getPassword());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getToken(), user.getToken());
        assertEquals(found.getUserStatus(), user.getUserStatus());
    }

    @Test
    public void findByUserId_success() {
        // when
        User found = userRepository.findByUserId(user.getUserId());

        // then
        assertNotNull(found.getUserId());
        assertEquals(found.getPassword(), user.getPassword());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getToken(), user.getToken());
        assertEquals(found.getUserStatus(), user.getUserStatus());
    }
}

 */