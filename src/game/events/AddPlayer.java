package game.events;

import java.io.Serializable;

import game.character.Player;

public class AddPlayer implements Event, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 521068336011976524L;
	private final Player player;

	public AddPlayer(Player player) {
		super();
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}
	
	
}
