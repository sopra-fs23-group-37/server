package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("gameRepository")
public interface GameRepository extends JpaRepository<Game, Long> {

    Game findByGameId(Long gameId);

    List<Game> findByGameStatusAndIsPrivate(GameStatus gameStatus, Boolean isPrivate);

    Game findByGameCode(String gameCode);

    Game findByGuestSessionId(String guestSessionId);

    Game findByHostSessionId(String hostSessionId);

}