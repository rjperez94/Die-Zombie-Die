package game.events;

import java.io.Serializable;

import game.character.Player;

/**
 *
 * @author Ronni Perez "perezronn"
 *
 */
public class ApplyItem implements Event, Serializable {
	private static final long serialVersionUID = -5867197969716208087L;
	private final Player player;

	public ApplyItem(Player player) {
		super();
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}
}
