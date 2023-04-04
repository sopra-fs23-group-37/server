package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.constant.SessionStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Session;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
public interface SessionDTOMapper {

    SessionDTOMapper INSTANCE = Mappers.getMapper(SessionDTOMapper.class);

    @Mapping(source = "sessionId", target = "sessionId")
    @Mapping(source = "game", target = "game")
    @Mapping(source = "host", target = "host")
    @Mapping(source = "guest", target = "guest")
    @Mapping(source = "spectators", target = "spectators")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "sessionStatus", target = "sessionStatus")
    @Mapping(source = "identifier", target = "identifier")
    @Mapping(source = "isPrivate", target = "isPrivate")
    SessionGetDTO convertEntityToSessionGetDTO(Session session);

    @Mapping(source = "host", target = "host")
    @Mapping(source = "isPrivate", target = "isPrivate")
    Session convertSessionPostDTOtoEntity(SessionPostDTO sessionPostDTO);


    @Mapping(source = "sessionId", target = "sessionId")
    @Mapping(source = "game", target = "game")
    @Mapping(source = "host", target = "host")
    @Mapping(source = "guest", target = "guest")
    @Mapping(source = "spectators", target = "spectators")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "sessionStatus", target = "sessionStatus")
    Session convertSessionPutDTOtoEntity(SessionPutDTO sessionPutDTO);



}
