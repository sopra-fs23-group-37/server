package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.service.LogoutService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(LogoutController.class)
public class LogoutControllerTest {

    @Autowired
    private MockMvc mockMvc;
    //private MockMvc mockMvc1;

    @MockBean
    private LogoutService logoutService;
    //private final Logout logout = new Logout();
    //private final LoginPutDTO loginPutDTO = new LoginPutDTO();


    @Test
    public void logoutUser() throws Exception {

        User user = new User();
        user.setUserId(3L);
        user.setUsername("tiger");
        user.setPassword("tiger123");
        user.setToken("3");
        user.setUserStatus(UserStatus.ONLINE);

        // when
        MockHttpServletRequestBuilder putRequest = put("/logout/3").header("token", "3");

        // then
        mockMvc.perform(putRequest).andExpect(status().isNoContent());


    }



    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input
     * can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     *
     * @param object
     * @return string
     */
    // private String asJsonString(final Object object) {
    //     try {
    //         return new ObjectMapper().writeValueAsString(object);
    //     } catch (JsonProcessingException e) {
    //         throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
    //                 String.format("The request body could not be created.%s", e.toString()));
    //     }
    // }
}