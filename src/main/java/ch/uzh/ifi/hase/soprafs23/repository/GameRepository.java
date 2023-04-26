package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("gameRepository")
public interface GameRepository extends JpaRepository<Game, Long> {

    Game findByGameId(Long GameId);

    List<Game> findByGameStatus(GameStatus GameStatus);

    Game findByGuestSessionId(String GuestSessionId);

    Game findByHostSessionId(String HostSessionId);

}