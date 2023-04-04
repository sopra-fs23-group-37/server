package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Session;
import ch.uzh.ifi.hase.soprafs23.rest.dto.SessionGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.SessionPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.SessionDTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.SessionService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SessionController {


    private final SessionService sessionService;
    private final UserService userService;

    SessionController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @GetMapping("/sessions")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<SessionGetDTO> getPublicSessions() {
        // fetch all sessions in the internal representation
        List<Session> publicSessions = sessionService.getPublicSessions();
        List<SessionGetDTO> sessionGetDTOs = new ArrayList<>();

        // convert each session to the API representation
        for (Session session : publicSessions) {
            sessionGetDTOs.add(SessionDTOMapper.INSTANCE.convertEntityToSessionGetDTO(session));
        }
        return sessionGetDTOs;
    }

    @PostMapping("/sessions")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public SessionGetDTO createSession(@RequestBody SessionPostDTO sessionPostDTO) {
        // convert API session to internal representation
        Session sessionInput = SessionDTOMapper.INSTANCE.convertSessionPostDTOtoEntity(sessionPostDTO);

        // create Session
        Session createdSession = sessionService.createSession(sessionInput);

        // convert internal representation of session back to API
        return SessionDTOMapper.INSTANCE.convertEntityToSessionGetDTO(createdSession);
    }

    @GetMapping("/sessions/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SessionGetDTO getSession(@PathVariable Long sessionId) {

        Session session = sessionService.getSession(sessionId);
        SessionGetDTO sessionGetDTO = SessionDTOMapper.INSTANCE.convertEntityToSessionGetDTO(session);

        return sessionGetDTO;
    }

}
