package ch.uzh.ifi.hase.soprafs23.entity;

public class PlayerJoinMessage {

	private User player;

	public PlayerJoinMessage() {
	}

	public PlayerJoinMessage(User player) {
		this.player = player;
	}

	public User getPlayer() {
		return player;
	}
}
