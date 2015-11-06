package game.item;

import java.io.Serializable;

import game.character.Player;
import game.graphics.Sprite;
import game.item.type.Item;

/**
 * This represent a Phaser. It will fire a beam when used
 * @author Ronni Perez "perezronn"
 *
 */
public class Phaser extends Item  implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 5848382905171560427L;

	/**
	 * Constructor
	 * @param name - item alias
	 * @param xStart - initial x position
	 * @param yStart - initial y position
	 * @param zStart - initial z position
	 * @param sprite - item graphics
	 */
	public Phaser (String name, float xStart, float yStart, float zStart, Sprite sprite) {
		super(name, xStart, yStart, zStart, sprite);
	}

	@Override
	public void use(Player player) {
		player.incrementBullets(15);
	}

	/**
	 * Don't use this constructor should only be used when loading from xml
	 */
	public Phaser() {

	}

}
