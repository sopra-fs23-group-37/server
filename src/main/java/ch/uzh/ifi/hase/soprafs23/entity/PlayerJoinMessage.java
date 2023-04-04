package ch.uzh.ifi.hase.soprafs23.entity;

public class PlayerJoinMessage {

	private Long playerId;

	public PlayerJoinMessage() {
	}

	public PlayerJoinMessage(Long playerId) {
		this.playerId = playerId;
	}

	public Long getPlayerId() {
		return playerId;
	}
}
