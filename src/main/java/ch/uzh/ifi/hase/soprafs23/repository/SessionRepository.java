package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.constant.SessionStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("sessionRepository")
public interface SessionRepository extends JpaRepository<Session, Long> {

    Session findBySessionId(Long SessionId);

    List<Session> findAllBySessionStatusAndIsPrivate(SessionStatus sessionStatus, boolean isPrivate);
}
