package game.events;

import java.io.Serializable;

import game.character.Player;

/**
 *
 * @author Ronni Perez "perezronn"
 *
 */
public class Respawn implements Event, Serializable {
	private static final long serialVersionUID = 7997723402668340426L;
	private final Player player;

	public Respawn(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

}
