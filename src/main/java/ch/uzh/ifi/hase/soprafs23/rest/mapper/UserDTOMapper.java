package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
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
public interface UserDTOMapper {

    UserDTOMapper INSTANCE = Mappers.getMapper(UserDTOMapper.class);

    //@Mapping(source = "name", target = "name")
    @Mapping(target = "userId", ignore = true)
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(target = "birthday", ignore = true)
    @Mapping(target = "creation_date", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "userStatus", ignore = true)
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);


    //@Mapping(source = "name", target = "name")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "userStatus", target = "userStatus")
    @Mapping(source = "creation_date", target = "creation_date")
    @Mapping(source = "birthday", target = "birthday")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "userStatus", target = "userStatus")
    @Mapping(source = "creation_date", target = "creation_date")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(target = "token", ignore = true)
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);



}
