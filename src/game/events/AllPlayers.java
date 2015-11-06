package game.events;

import game.character.Player;

import java.io.Serializable;
import java.util.List;

public class AllPlayers implements Event, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4497151164254878638L;
	private final List<Player> players;

	public AllPlayers (List<Player> p) {
		this.players =p;
	}

	public List<Player> getPlayers() {
		return players;
	}


}
