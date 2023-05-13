package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  private User user;
  private UserPostDTO userPostDTO;

  @MockBean
  private UserPutDTO userPutDTO;

  @BeforeEach
  void setup() {
    user = new User();
    user.setUserId(999L);
    user.setUsername("firstname@lastname");
    user.setUserStatus(UserStatus.OFFLINE);
    user.setPassword("testPassword");
    user.setCreation_date(new Date());

    userPostDTO = new UserPostDTO();
    userPostDTO.setUsername("testUsername");
    userPostDTO.setPassword("testPassword");

    userPutDTO = new UserPutDTO();
    userPutDTO.setUsername("otherUsername");
    userPutDTO.setBirthday(new Date(552218400000L));
    userPutDTO.setAvatarUrl("otherAvatarUrl");

  }

  // 1. There is one user in the list, because we created just one user, so 200 OK
  // expected
  @Test
  public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
    List<User> allUsers = Collections.singletonList(user);
    given(userService.getUsers()).willReturn(allUsers);

    MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

    mockMvc.perform(getRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].username", is(user.getUsername())))
        .andExpect(jsonPath("$[0].password", is(user.getPassword())))
        .andExpect(jsonPath("$[0].userStatus", is(user.getUserStatus().toString())));
  }

  // 2. Create user: Status Code 201 CREATED expected
  @Test
  public void createUser_validInput_userCreated() throws Exception {

    user.setUserId(1L);
    given(userService.createUser(Mockito.any())).willReturn(user);

    // Test with valid input
    MockHttpServletRequestBuilder postRequestValid = post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO));

    mockMvc.perform(postRequestValid)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId", is(user.getUserId().intValue())))
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.password", is(user.getPassword())))
        .andExpect(jsonPath("$.userStatus", is(user.getUserStatus().toString())));

  }

  // 3. Create user: new user failed because username already exists: Status Code
  // 409 CONFLICT expected
  @Test
  public void createUser_invalidInput() throws Exception {
    given(userService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

    MockHttpServletRequestBuilder postRequest = post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO));

    mockMvc.perform(postRequest)
        .andExpect(status().isConflict());

  }

  // 4. Get user: profile with userID: Status Code 200 OK expected
  @Test
  public void getUser_validInput_returnJSONUser() throws Exception {
    // this mocks the UserService -> we define above what the userService should
    // return when getUser() is called
    given(userService.getUserById(999L)).willReturn(user);

    // when
    MockHttpServletRequestBuilder getRequest = get("/users/999").contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$.userId", is(user.getUserId().intValue())))
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.password", is(user.getPassword())))
        .andExpect(jsonPath("$.userStatus", is(user.getUserStatus().toString())));

  }

  // 5. Get user: User with userID was not found: Status Code 404 NOT_FOUND
  // expected
  @Test
  public void getUser_invalidInput() throws Exception {
    given(userService.getUserById(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

    MockHttpServletRequestBuilder getRequest = get("/users/999").contentType(MediaType.APPLICATION_JSON);

    mockMvc.perform(getRequest).andExpect(status().isNotFound());
  }

  // 6. Update user: Status Code 204 NO_CONTENT expected
  @Test
  public void updateUser_validInput_userUpdated() throws Exception {

    doNothing().when(userService).updateUser(1L, user);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder putRequest = put("/users/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPutDTO));

    // then
    mockMvc.perform(putRequest)
        .andExpect(status().isNoContent());
  }

  // 7. Update user: User with userID was not found: Status Code 404 NOT_FOUND
  // expected
  @Test
  public void updateUser_invalidInput() throws Exception {

    doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(userService).updateUser(Mockito.anyLong(),
        Mockito.any());

    MockHttpServletRequestBuilder putRequest = put("/users/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPutDTO));

    mockMvc.perform(putRequest)
        .andExpect(status().isNotFound());
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