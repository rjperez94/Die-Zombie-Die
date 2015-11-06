package game.item;

import java.io.Serializable;

import game.Utilities;
import game.character.Player;
import game.graphics.Sprite;
import game.item.type.Item;

/**
 * This represents a pill. It can be consumed by a Player
 * @author Ronni Perez "perezronn"
 *
 */
public class Pill extends Item  implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 331808080846938033L;
	private final int boost;

	/**
	 * Constructor
	 * @param name - item alias
	 * @param xStart - initial x position
	 * @param yStart - initial y position
	 * @param zStart - initial z position
	 * @param boost - the randomly generated health increment
	 * @param sprite - item graphics
	 */
	public Pill (String name, float xStart, float yStart, float zStart, int boost, Sprite sprite) {
		super(name, xStart, yStart, zStart, sprite);
		this.boost = boost;
	}

	@Override
	public void use(Player player) {
		player.replenish(boost);
	}

	/**
	 * @return the boost
	 */
	public int getBoost() {
		return boost;
	}


	//Load Save related methods
	/**
	 * Don't use this constructor should only be used when loading from xml
	 */
	public Pill(){
		this.boost = Utilities.randNum(1,100);
	}
}
