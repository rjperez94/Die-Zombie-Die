package game.item;

import java.io.Serializable;

import game.character.Player;
import game.graphics.Sprite;
import game.item.type.Equipment;

/**
 * This represents a vest - a shield that gives minimal health boost
 * @author Ronni Perez "perezronn"
 *
 */
public class Vest extends Equipment implements Serializable  {

	/**
	 *
	 */
	private static final long serialVersionUID = -8144580737748445934L;

	/**
	 * Constructor
	 * @param name - equipment alias
	 * @param xStart - initial x position
	 * @param yStart - initial y position
	 * @param zStart - initial z position
	 * @param sprite - equipment graphic
	 */
	public Vest(String name, float xStart, float yStart, float zStart, Sprite sprite) {
		super(name, xStart, yStart, zStart, 0, 20, sprite);
	}

	@Override
	public void use(Player player) {
		player.replenish(this.getDefence());
	}

	//Load Save only
	/**
	 * Don't use this constructor should only be used when loading from xml
	 */
	public Vest(){
		
	}
}
