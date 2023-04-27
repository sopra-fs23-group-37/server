package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePutDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface GameDTOMapper {

    GameDTOMapper INSTANCE = Mappers.getMapper(GameDTOMapper.class);

    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "host", target = "host")
    @Mapping(source = "guest", target = "guest")
    @Mapping(source = "winner", target = "winner")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "gameStatus", target = "gameStatus")
    @Mapping(source = "hostStatus", target = "hostStatus")
    @Mapping(source = "guestStatus", target = "guestStatus")
    @Mapping(source = "startingPlayer", target = "startingPlayer")
    @Mapping(source = "currentRound", target = "currentRound")
    @Mapping(source = "hostSessionId", target = "hostSessionId")
    @Mapping(source = "guestSessionId", target = "guestSessionId")
    GameGetDTO convertEntityToGameGetDTO(Game game);

    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "host", target = "host")
    @Mapping(source = "guest", target = "guest")
    @Mapping(source = "winner", target = "winner")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "gameStatus", target = "gameStatus")
    @Mapping(target = "hostStatus", ignore = true)
    @Mapping(target = "guestStatus", ignore = true)
    @Mapping(target = "startingPlayer", ignore = true)
    @Mapping(target = "currentRound", ignore = true)
    @Mapping(target = "totalRounds", ignore = true)
    @Mapping(target = "hostPoints", ignore = true)
    @Mapping(target = "guestPoints", ignore = true)
    @Mapping(target = "hostSessionId", ignore = true)
    @Mapping(target = "guestSessionId", ignore = true)
    @Mapping(target = "endGameReason", ignore = true)
    Game convertGamePostDTOtoEntity(GamePostDTO gamePostDTO);

    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "host", target = "host")
    @Mapping(source = "guest", target = "guest")
    @Mapping(source = "winner", target = "winner")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "gameStatus", target = "gameStatus")
    @Mapping(target = "hostStatus", ignore = true)
    @Mapping(target = "guestStatus", ignore = true)
    @Mapping(target = "startingPlayer", ignore = true)
    @Mapping(target = "currentRound", ignore = true)
    @Mapping(target = "totalRounds", ignore = true)
    @Mapping(target = "hostPoints", ignore = true)
    @Mapping(target = "guestPoints", ignore = true)
    @Mapping(target = "hostSessionId", ignore = true)
    @Mapping(target = "guestSessionId", ignore = true)
    @Mapping(target = "endGameReason", ignore = true)
    Game convertGamePutDTOtoEntity(GamePutDTO gamePutDTO);

}
