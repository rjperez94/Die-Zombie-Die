package game.graphics;

import java.awt.Graphics2D;

/**
 * <code>Sprite</code> is an abstract class designed to allow both
 * <code>StaticSprite</code> and <code>AnimatedSprite</code> objects to be
 * stored together and called using the same methods.
 * 
 * @author Brendan Goodenough
 * @version 0.1.0
 */

public abstract class Sprite {
	protected float x, y;
	protected int width, height;

	/**
	 * Updates the logic of the <code>Sprite</code>.
	 */
	public abstract void update();

	/**
	 * Draws the <code>Sprite</code> to the screen.
	 * 
	 * @param g2 <code>Graphics2D</code> object to use for rendering
	 */
	public abstract void render(Graphics2D g2);

	/**
	 * Draws the <code>Sprite</code> to the screen with specified x and y
	 * location. This location does not alter the x and y coordinates of the
	 * sprite itself.
	 * 
	 * @param g2 <code>Graphics2D</code> object to use for rendering
	 * @param x location in the x direction to draw the sprite
	 * @param y location in the y direction to draw the sprite
	 */
	public abstract void render(Graphics2D g2, float x, float y);

	/**
	 * Returns the x location of the sprite.
	 * 
	 * @return x location
	 */
	public float getX() {
		return x;
	}

	/**
	 * Returns the y location of the sprite.
	 * 
	 * @return y
	 */
	public float getY() {
		return y;
	}

	/**
	 * Returns the width of the sprite.
	 * 
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the sprite.
	 * 
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the x location of the sprite.
	 * 
	 * @param x location in the x direction
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Sets the y location of the sprite.
	 * 
	 * @param y location in the y direction
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Sets the width of the sprite.
	 * 
	 * @param width sprite width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Sets the height of the sprite.
	 * 
	 * @param height sprite height
	 */
	public void setHeight(int height) {
		this.height = height;
	}
}
