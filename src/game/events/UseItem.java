package game.events;

import java.io.Serializable;

import game.character.Player;
import game.item.type.Item;

/**
 *
 * @author Ronni Perez "perezronn"
 *
 */
public class UseItem implements Event, Serializable {
	private static final long serialVersionUID = -8459193753659462333L;
	private final Item item;
	private final Player player;

	public UseItem(Player player, Item item) {
		super();
		this.player =player;
		this.item = item;
	}

	public Player getPlayer() {
		return player;
	}

	public Item getItem() {
		return item;
	}

}
