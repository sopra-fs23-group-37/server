package ch.uzh.ifi.hase.soprafs23.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.GameDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private GameService gameService;
    private User host;
    private Game game1;
    private Game game2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // initialize host
        host = new User();
        host.setUserId(1L);
        host.setUsername("testuser");
        // initialize game1
        game1 = new Game();
        game1.setGameId(1L);
        game1.setHost(host);
        // initialize game2
        game2 = new Game();
        game2.setGameId(2L);
        game2.setHost(host);
        game2.setGameStatus(GameStatus.WAITING);

        // mock behavior of gameService
        given(gameService.getPublicGames()).willReturn(Arrays.asList(game1, game2));
        given(gameService.createGame(any(Game.class))).willReturn(game2);
        given(gameService.joinGame(any())).willReturn(game1);

        // mock behavior of userService
        given(userService.getUserById(anyLong())).willReturn(host);
    }

        @Test
        public void givenGames_whenGetOpenGames_thenReturnJsonArray() throws Exception {

                List<Game> openGames = new ArrayList<>();
                openGames.add(game1);
                openGames.add(game2);
                given(gameService.getPublicGames()).willReturn(openGames);

                // when/then
                mockMvc.perform(get("/games"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].gameId", is(1)))
                                .andExpect(jsonPath("$[1].gameId", is(2)));
        }

        @Test
        public void givenOpenGames_whenGetOpenGames_thenReturnJsonArray() throws Exception {

                // given the open games are returned by the Service
                given(gameService.getPublicGames()).willReturn(Arrays.asList(game1, game2));

                // test return
                mockMvc.perform(get("/games"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].gameId", is(1)))
                                .andExpect(jsonPath("$[1].gameId", is(2)));

        }

        @Test
        public void givenGame_whenCreateGame_thenReturnJsonArray() throws Exception {

                // given the created Game is returned by the Service
                GamePostDTO gamePostDTO = new GamePostDTO();
                Game game = GameDTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);
                game.setGameId(1L);
                given(gameService.createGame(any(Game.class))).willReturn(game);

                // build post request
                MockHttpServletRequestBuilder postRequest = post("/games")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"My Test Game\"}");

                // test return
                mockMvc.perform(postRequest).andExpect(status().isCreated())
                                .andExpect(jsonPath("$.gameId", is(1)));

        }

    @Test
    public void givenNoGame_whenCreateGame_thenReturnJson() throws Exception {

        // use Mockito to mock the behavior of userService to return the host User object when any Long is passed to getUserById()
        given(userService.getUserById(anyLong())).willReturn(host);

        // given the Game is returned by the Service
        Game game = new Game();
        game.setGameId(999L);
        game.setHost(host);
        game.setGameStatus(GameStatus.WAITING);

        // use Mockito to mock the behavior of gameService to return the game object when any Game object is passed to createGame()
        given(gameService.createGame(any(Game.class))).willReturn(game);

        // build post request with JSON content
        MockHttpServletRequestBuilder postRequest = post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"hostId\":\"1\",\"totalRounds\":\"10\"}");

        // perform the POST request and expect a HTTP 201 Created status code
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                // expect the returned JSON object to have the correct gameId, host userId, host username, and game status
                .andExpect(jsonPath("$.gameId", is(999)))
                .andExpect(jsonPath("$.host.userId", is(1)))
                .andExpect(jsonPath("$.host.username", is("testuser")))
                .andExpect(jsonPath("$.gameStatus", is("WAITING")));
    }


        @Test
        public void givenGames_whenJoin_thenReturnJsonArray() throws Exception {

                // given the Game is returned by the Service
                Game game = new Game();
                game.setGameId(999L);
                given(gameService.joinGame(any())).willReturn(game);

                // build put request
                MockHttpServletRequestBuilder putRequest = put("/games/join/1")
                                .contentType(MediaType.APPLICATION_JSON);

                // test return
                mockMvc.perform(putRequest).andExpect(status().isOk())
                                .andExpect(jsonPath("$.gameId", is(999)));

        }

}