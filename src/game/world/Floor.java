package game.world;

import java.io.Serializable;

import game.graphics.Sprite;

/**
 * This represents a Floor. It is an empty area
 * @author Ronni Perez "perezronn"
 *
 */
public class Floor extends GameObject  implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6682624883285038526L;

	/**
	 * Constructor
	 * @param name - floor alias
	 * @param xStart - initial x position
	 * @param yStart - initial y position
	 * @param zStart - initial z position
	 * @param sprite
	 */
	public Floor (String name, float xStart, float yStart, float zStart, Sprite sprite) {
		super(name, xStart, yStart, zStart, sprite);
	}

}
