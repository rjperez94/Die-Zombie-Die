package game.events;

import java.io.Serializable;

import game.character.Character;
import game.character.Player;

/**
 *
 * @author Ronni Perez "perezronn"
 *
 */
public class Collide implements Event, Serializable {
	private static final long serialVersionUID = 8615479500686119877L;
	private final Player player;
	private final Character character;

	public Collide(Player player, Character c) {
		this.player = player;
		this.character = c;
	}

	public Player getPlayer() {
		return player;
	}

	public Character getCharacter() {
		return character;
	}


}
