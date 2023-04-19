package ch.uzh.ifi.hase.soprafs23.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
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

        @BeforeEach
        public void setUp() {
                MockitoAnnotations.openMocks(this);
        }

        @Test
        public void givenGames_whenGetOpenGames_thenReturnJsonArray() throws Exception {
                // given
                Game game1 = new Game();
                game1.setGameId(1L);
                Game game2 = new Game();
                game2.setGameId(2L);
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
                Game game1 = new Game();
                game1.setGameId(1L);
                Game game2 = new Game();
                game2.setGameId(2L);
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

                // given the User is returned by the Service
                User host = new User();
                host.setUserId(1L);
                host.setUsername("testuser");

                given(userService.getUserById(any())).willReturn(host);

                // given the Game is returned by the Service
                Game game = new Game();
                game.setGameId(999L);
                game.setHost(host);
                game.setGameStatus(GameStatus.WAITING);
                given(gameService.createGame(any())).willReturn(game);

                // build post request
                MockHttpServletRequestBuilder postRequest = post("/games")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"hostId\":\"1\",\"totalRounds\":\"10\"}");

                // test return
                mockMvc.perform(postRequest).andExpect(status().isCreated())
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