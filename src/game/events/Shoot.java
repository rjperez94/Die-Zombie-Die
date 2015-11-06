package game.events;

import java.io.Serializable;

import game.character.Player;

/**
 *
 * @author Ronni Perez "perezronn"
 *
 */
public class Shoot implements Event, Serializable{
	private static final long serialVersionUID = -4234154958390123745L;
	private final Player player;

	public Shoot(Player player) {
		super();
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}


}
