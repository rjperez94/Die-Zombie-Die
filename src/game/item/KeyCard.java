package game.item;

import java.io.Serializable;

import game.character.Player;
import game.graphics.Sprite;
import game.item.type.Item;

/**
 * This represents the KeyCard. It can be possessed by a Player
 * @author Ronni Perez "perezronn"
 *
 */
public class KeyCard extends Item implements Serializable  {
	/**
	 *
	 */
	private static final long serialVersionUID = -5145880420281083736L;
	private boolean heldByPlayer;

	/**
	 * Constructor
	 * @param name - key alias
	 * @param xStart - initial x position
	 * @param yStart - initial y position
	 * @param zStart - initial z position
	 * @param sprite - item graphics
	 */
	public KeyCard (String name, float xStart, float yStart, float zStart, Sprite sprite) {
		super(name, xStart, yStart, zStart, sprite);
		this.heldByPlayer = false;
	}

	@Override
	public void use(Player player) {
		if (player.addToInventory(this)) {
			this.heldByPlayer = true;
		}
	}

	//Load Save methods

	/**
	 * Don't use this constructor should only be used when loading from xml
	 */
	public KeyCard(){

	}

	public boolean isHeldByPlayer() {
		return heldByPlayer;
	}

	public void setHeldByPlayer(boolean heldByPlayer) {
		this.heldByPlayer = heldByPlayer;
	}


}
