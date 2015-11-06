package game.item;

import java.io.Serializable;

import game.character.Player;
import game.graphics.Sprite;
import game.item.type.Equipment;

/**
 * This represents the ShieldOfAres - a Greek god of war
 * This gives a Player +50 health boost
 * @author Ronni Perez "perezronn"
 *
 */
public class ShieldOfAres extends Equipment  implements Serializable  {

	/**
	 *
	 */
	private static final long serialVersionUID = 4962308996757594918L;

	/**
	 * Constructor
	 * @param name - equipment alias
	 * @param xStart - initial x position
	 * @param yStart - initial y position
	 * @param zStart - initial z position
	 * @param sprite - equipment graphic
	 */
	public ShieldOfAres(String name, float xStart, float yStart, float zStart, Sprite sprite) {
		super(name, xStart, yStart, zStart, 0, 50, sprite);
	}

	@Override
	public void use(Player player) {
		player.replenish(this.getDefence());
	}

	//Load Save Code

	/**
	 * Don't use this constructor should only be used when loading from xml
	 */
	public ShieldOfAres(){

	}

}
