package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.SessionStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Session;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.SessionRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.util.*;

/**
 * Session Service
 * This class is the "worker" and responsible for all functionality related to
 * the session
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class SessionService {

    private Random random = new Random();

    private final Logger log = LoggerFactory.getLogger(LoginService.class);

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(@Qualifier("userRepository") UserRepository userRepository, @Qualifier("sessionRepository") SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public List<Session> getPublicSessions() {
        List<Session> publicSessions = this.sessionRepository.findAllBySessionStatusAndIsPrivate(SessionStatus.CREATED, false);
        return publicSessions;
    }

    public Session createSession(Session newSession) {
        // update Session status
        newSession.setSessionStatus(SessionStatus.CREATED);
        newSession.setCreatedDate(new Date());

        // create identifier
        newSession.setIdentifier(createRandomNumberString());

        // find host
        String baseErrorMessage = "Host with id %x was not found";
        Long hostId = newSession.getHost().getUserId();
        System.out.println("Yes, %x");

        User host = userRepository.findByUserId(hostId);
        if(host == null) {
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage,hostId));
        }

        // set host to user
        newSession.setHost(host);
        newSession.setCreatedDate(new Date());

        // save to repo and flush
        newSession = sessionRepository.save(newSession);
        sessionRepository.flush();

        log.debug("Created Information for Session: {}", newSession);
        return newSession;
    }

    public Session getSession(Long sessionId) {
        return this.sessionRepository.findBySessionId(sessionId);
    }

    private String createRandomNumberString() {
        int number = random.nextInt(999999);

        // to be super safe one could save the number and compare against a runtime array of previously rolled numbers

        return String.format("%06d", number);
    }


}
