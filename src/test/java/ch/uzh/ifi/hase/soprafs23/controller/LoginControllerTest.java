package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LoginPutDTO;
import ch.uzh.ifi.hase.soprafs23.entity.Login;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.service.LoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;
    private final LoginPutDTO loginPutDTO = new LoginPutDTO();

    @Test
    public void loginUser_checkJsonReturned() throws Exception {
        // given
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setUserStatus(UserStatus.OFFLINE);
        user.setCreation_date(new Date());
        user.setUserId(1L);
        user.setToken("1");
        user.setCreation_date(new Date());

        Login login = new Login();
        login.setUserId(user.getUserId());
        login.setUsername(user.getUsername());
        login.setPassword(user.getPassword());

        LoginPutDTO loginPutDTO = new LoginPutDTO();
        loginPutDTO.setUsername(user.getUsername());
        loginPutDTO.setPassword(user.getPassword());
        loginPutDTO.setSuccessful(true);

        Mockito.when(loginService.createLogin(Mockito.any())).thenReturn(login);

        // when
        MockHttpServletRequestBuilder request = put("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginPutDTO));

        // then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(login.getUserId().intValue())))
                .andExpect(jsonPath("$.username", is(login.getUsername())))
                .andExpect(jsonPath("$.password", is(login.getPassword())))
                .andExpect(jsonPath("$.successful", is(login.getSuccessful())))
                .andExpect(jsonPath("$.token", is(login.getToken())))
                .andExpect(jsonPath("$.loginDate", is(login.getLoginDate())));
    }

    @Test
    public void loginUser_notExisting() throws Exception {
        //additional given
        loginPutDTO.setUsername("tiger");
        loginPutDTO.setPassword("tiger123");

        Mockito.when(loginService.createLogin(Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user was not found"));

        // when
        MockHttpServletRequestBuilder postRequest = put("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginPutDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest());
    }
    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input
     * can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     *
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}