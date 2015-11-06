package game.world;

import java.awt.Graphics2D;
import java.io.Serializable;

import game.graphics.Sprite;

/**
 * This represents a Wall
 * @author Ronni Perez "perezronn"
 *
 */
public class Wall extends GameObject  implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 2233314377729563367L;
	final boolean tall;


	/**
	 * Constructor
	 * @param name - wall alias
	 * @param xStart - initial x position
	 * @param yStart - initial y position
	 * @param zStart - initial z position
	 * @param sprite - object graphics
	 * @param tall	 - true if this wall can cover characters
	 */
	public Wall (String name, float xStart, float yStart, float zStart, Sprite sprite, boolean tall) {
		super(name, xStart, yStart, zStart, sprite);
		this.tall = tall;
	}

	/**
	 * Renders this object to the screen using custom rendering if it can hide characters
	 */
	@Override
	public void render(Graphics2D g2, float xOffset, float yOffset) {
		if(tall){
			if (sprite != null) {
				sprite.render(g2, xOffset + (xLoc * World.TILE_SIZE), (int)(yOffset + (yLoc * World.TILE_SIZE)-.5*World.TILE_SIZE));
			}
		}
		else{
			super.render(g2, xOffset, yOffset);
		}
	}

	public boolean isTall() {
		return tall;
	}
}
