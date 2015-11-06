package game.world;

import game.graphics.Sprite;

import java.awt.Graphics2D;

import javafx.geometry.BoundingBox;

/**
 * This ensures that objects which are appearing on the
 * screen has a sprite such that it can be drawn.
 *
 * @author Ronni Perez "perezronn"
 *
 */
public abstract class GameObject {
	protected String name;

	protected float xLoc;		//location
	protected float yLoc;
	protected float zLoc;
	//may be null, if GameObject is animated
	protected Sprite sprite;	//graphic

	/**
	 * Constructor
	 * @param name - item alias
	 * @param xStart - initial x position
	 * @param yStart - initial y position
	 * @param zStart - initial z position
	 */
	public GameObject (String name, float xStart, float yStart, float zStart, Sprite sprite) {
		this.name = name;
		this.xLoc = xStart;
		this.yLoc = yStart;
		this.zLoc = zStart;
		this.sprite = sprite;
	}

	/**
	 * Renders this object to the screen
	 * @param g2 - the graphics object
	 * @param xOffset - x coordinate centre offset
	 * @param yOffset - y coordinate centre offset
	 */
	public void render(Graphics2D g2, float xOffset, float yOffset) {
		if (sprite != null) {
			sprite.render(g2, xOffset + (xLoc * World.TILE_SIZE), yOffset + (yLoc * World.TILE_SIZE));
		}
	}

	/**
	 * Returns a BoundingBox that corresponds to the position of this object's FEET if a Character
	 * <br> or the topLeft if it's not.
	 * @return
	 */
	public BoundingBox boundingBox() {
		return new BoundingBox(xLoc*World.TILE_SIZE, yLoc*World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE);
	}

	//GETTERS/SETTERS
	public String getName() {
		return name;
	}

	public float getX() {
		return xLoc;
	}

	public void setX(float x) {
		this.xLoc = x;
	}

	public float getY() {
		return yLoc;
	}

	public void setY(float y) {
		this.yLoc = y;
	}

	public float getZ() {
		return zLoc;
	}

	public void setZ(float z) {
		this.zLoc = z;
	}

	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GameObject [name=" + name + ", xLoc=" + xLoc + ", yLoc=" + yLoc
				+ ", zLoc=" + zLoc + ", sprite=" + sprite + "]";
	}


	//Load Save only
	/**
	 * Don't use this constructor should only be used when loading from xml
	 */
	public GameObject(){

	}
}
