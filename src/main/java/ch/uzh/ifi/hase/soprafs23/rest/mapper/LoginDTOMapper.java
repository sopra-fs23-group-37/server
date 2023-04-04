package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Login;
import ch.uzh.ifi.hase.soprafs23.entity.Session;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Date;

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
public interface LoginDTOMapper {

    LoginDTOMapper INSTANCE = Mappers.getMapper(LoginDTOMapper.class);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "loginDate", target = "loginDate")
    @Mapping(source = "successful", target = "successful")
    @Mapping(target = "token", ignore = true)
    Login convertLoginPostDTOtoEntity(LoginPostDTO loginPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "loginDate", target = "loginDate")
    @Mapping(source = "successful", target = "successful")
    @Mapping(source = "token", target = "token")
    LoginGetDTO convertEntityToLoginGetDTO(Login login);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "loginDate", target = "loginDate")
    @Mapping(source = "successful", target = "successful")
    @Mapping(target = "token", ignore = true)
    Login convertLoginPutDTOtoEntity(LoginPutDTO loginPutDTO);


}
