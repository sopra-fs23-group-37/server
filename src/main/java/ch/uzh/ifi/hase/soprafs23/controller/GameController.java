package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.GameDTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {

    private final GameService gameService;
    //private final UserService userService;

    GameController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        //this.userService = userService;
    }

    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GameGetDTO> getPublicGames() {
        // fetch all games in the internal representation
        List<Game> publicGames = gameService.getPublicGames();
        List<GameGetDTO> gameGetDTOs = new ArrayList<>();

        // convert each game to the API representation
        for (Game game : publicGames) {
            gameGetDTOs.add(GameDTOMapper.INSTANCE.convertEntityToGameGetDTO(game));
        }
        return gameGetDTOs;
    }

    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameGetDTO createGame(@RequestBody GamePostDTO gamePostDTO) {
        // convert API game to internal representation
        Game gameInput = GameDTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);

        // create Game
        Game createdGame = gameService.createGame(gameInput);

        // convert internal representation of game back to API
        return GameDTOMapper.INSTANCE.convertEntityToGameGetDTO(createdGame);
    }

    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGame(@PathVariable Long gameId) {

        Game game = gameService.getGame(gameId);
        GameGetDTO gameGetDTO = GameDTOMapper.INSTANCE.convertEntityToGameGetDTO(game);

        return gameGetDTO;
    }

    @PutMapping("/games/join/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO joinGame(@PathVariable Long userId) {

        // join Game
        Game joinedGame = gameService.joinGame(userId);

        // convert internal representation of game back to API
        return GameDTOMapper.INSTANCE.convertEntityToGameGetDTO(joinedGame);
    }

}