package game.events;

import java.io.Serializable;

import game.Utilities.Direction;
import game.character.Player;

/**
 *
 * @author Ronni Perez "perezronn"
 *
 */
public class MovePlayer implements Event, Serializable {
	private static final long serialVersionUID = -7998020437808317076L;
	private final Player player;
	private final Direction dir;

	public MovePlayer(Player player, Direction dir) {
		super();
		this.player = player;
		this.dir = dir;
	}

	public Direction getDir() {
		return dir;
	}

	public Player getPlayer() {
		return player;
	}


}
