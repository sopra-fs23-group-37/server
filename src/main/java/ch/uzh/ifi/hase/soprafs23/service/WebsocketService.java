package ch.uzh.ifi.hase.soprafs23.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs23.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs23.constant.PlayerStatus;
import ch.uzh.ifi.hase.soprafs23.constant.Role;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Round;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.WSRoundStatusDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.WSDTOMapper;

@Service
@Transactional
public class WebsocketService {

    @Autowired
    private final SimpMessagingTemplate simp;

    private final Map<String, Timer> reconnectTimer = new HashMap<>();

    private final GameRepository gameRepository;

    public WebsocketService(GameRepository gameRepository, SimpMessagingTemplate simp) {
        this.simp = simp;
        this.gameRepository = gameRepository;
    }

    /**
     * method to send a dto to the lobby channel of a specific game
     * 
     * @param gameId the id of the game to send the payload to
     * @param dto    any data object, should be a dto with only data necessary for
     *               the client
     */
    public void sendToLobby(Long gameId, Object dto) {
        String destination = String.format("/topic/game/%d/lobby", gameId);
        this.simp.convertAndSend(destination, dto);
    }

    /**
     * method to send a dto to the game channel of a specific game
     * 
     * @param gameId the id of the game to send the payload to
     * @param dto    any data object, should be a dto with only data necessary for
     *               the client
     */
    public void sendToGame(Long gameId, Object dto) {
        String destination = String.format("/topic/game/%d/game", gameId);
        this.simp.convertAndSend(destination, dto);
    }

    /**
     * method to send a dto to the round channel of a specific game
     * 
     * @param gameId the id of the game to send the payload to
     * @param dto    any data object, should be a dto with only data necessary for
     *               the client
     */
    public void sendToRound(Long gameId, Object dto) {
        String destination = String.format("/topic/game/%d/round", gameId);
        this.simp.convertAndSend(destination, dto);
    }

    public void sendGameToUser(Long userId, Object dto) {
        String destination = String.format("/queue/user/%d/game", userId);
        this.simp.convertAndSend(destination, dto);
    }

    /**
     * method to send a player-specific round update to all the players in the game
     * 
     * @param game  the game that has an update
     * @param round the round for which to send an update
     */
    public void sendRoundUpdate(Game game, Round round) {
        // send to host
        sendRoundInfoToUser(game, round, game.getHost().getUserId());

        // send to guest
        sendRoundInfoToUser(game, round, game.getGuest().getUserId());
    }

    public void sendRoundInfoToUser(Game game, Round round, Long userId) {
        WSRoundStatusDTO dto = createWSRoundStatusDTOforUser(game, round, userId);
        String destination = String.format("/queue/user/%d/round", userId);

        this.simp.convertAndSend(destination, dto);
    }

    public WSRoundStatusDTO createWSRoundStatusDTOforUser(Game game, Round round, Long userId) {
        WSRoundStatusDTO dto = new WSRoundStatusDTO();

        // set status
        dto.setRoundStatus(round.getRoundStatus());

        // determine current players Role
        Role myRole = game.getHost().getUserId().equals(userId) ? Role.HOST : Role.GUEST;

        // determine which is current player and which is opponent player
        Player myPlayer = myRole.equals(Role.HOST) ? round.getHost() : round.getGuest();
        Player opponent = myRole.equals(Role.HOST) ? round.getGuest() : round.getHost();

        // set turn based on role
        dto.setMyTurn(round.getCurrentTurnPlayer().equals(myRole));

        // set cards
        dto.setMyCardsInHand(myPlayer.getCardsInHand());
        dto.setMyCardsInDiscard(myPlayer.getCardsInDiscard());
        dto.setOppCards(opponent.getCardsInHand().size());
        dto.setOppCardsInDiscard(opponent.getCardsInDiscard());
        dto.setCardsOnTable(round.getTableCards());
        dto.setDeckCards(round.getCardDeck().getRemaining() > 0);

        // set points
        // players points
        dto.setMyPointsTotalCards(myPlayer.getPointsTotalCards());
        dto.setMyPointClubs(myPlayer.getPointClubs());
        dto.setMyTwoOfClubs(myPlayer.getTwoOfClubs());
        dto.setMyTenOfDiamonds(myPlayer.getTenOfDiamonds());
        dto.setMyTotalPoints(myPlayer.getTotalPoints());
        // opponent points
        dto.setOppPointsTotalCards(opponent.getPointsTotalCards());
        dto.setOppPointClubs(opponent.getPointClubs());
        dto.setOppTwoOfClubs(opponent.getTwoOfClubs());
        dto.setOppTenOfDiamonds(opponent.getTenOfDiamonds());
        dto.setOppTotalPoints(opponent.getTotalPoints());

        return dto;

    }

    public void startReconnectTimer(int seconds, Game game, Role role, Long userId, String sessionId) {
        int milliseconds = seconds * 1000;
        this.reconnectTimer.put(userId + sessionId, new Timer());
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                disconnectPlayer(game, role);
            }
        };
        this.reconnectTimer.get(userId + sessionId).schedule(timerTask, milliseconds);
    }

    public synchronized void cancelDisconnect(Long userId, String oldSessionId) {
        // cancel the task to disconnect
        this.reconnectTimer.get(userId + oldSessionId).cancel();
    }

    public void disconnectPlayer(Game game, Role role) {

        // check which player disconnected and get their username
        String username = null;
        if (role.equals(Role.HOST)) {
            game.setHostStatus(PlayerStatus.DISCONNECTED);
            username = game.getHost().getUsername();
        } else if (role.equals(Role.GUEST)) {
            game.setGuestStatus(PlayerStatus.DISCONNECTED);
            username = game.getGuest().getUsername();
        }

        // update the status of the game and set the reason for the end of the game
        game.setGameStatus(GameStatus.DISCONNECTED);
        game.setEndGameReason("Player " + username + " unexpectedly disconnected from the game.");

        // save to ensure DB is up to date
        gameRepository.save(game);

        // send the update to the lobby and the game so that all players are informed
        sendToLobby(game.getGameId(), WSDTOMapper.INSTANCE.convertEntityToWSGameStatusDTO(game));
        sendToGame(game.getGameId(), WSDTOMapper.INSTANCE.convertEntityToWSGameStatusDTO(game));
    }

}
