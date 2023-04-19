package ch.uzh.ifi.hase.soprafs23.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;


@WebMvcTest(GameController.class)
public class GameControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private GameService gameService;


  @Test
  public void givenGames_whenJoin_thenReturnJsonArray() throws Exception {
        
    // given the Game is returned by the Service
    Game game = new Game();
    game.setGameId(999L);
    given(gameService.joinGame(Mockito.any())).willReturn(game);

    // build put request
    MockHttpServletRequestBuilder putRequest = put("/games/join/1")
                .contentType(MediaType.APPLICATION_JSON);

    // test return
    mockMvc.perform(putRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$.gameId", is(999)));

    }

    
}
