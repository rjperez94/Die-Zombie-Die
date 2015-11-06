package game.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * <code>StaticSprite</code> is a type of <code>Sprite</code> that holds and
 * displays a single image.
 *
 * @author Brendan Goodenough
 * @version 0.1.0
 */

public class StaticSprite extends Sprite {
	private BufferedImage sprite;

	/**
	 * Constructs a new <code>StaticSprite</code> with the specified image and
	 * location.
	 * 
	 * @param sprite <code>BufferedImage</code> for the sprite
	 * @param x initial x location
	 * @param y initial y location
	 */
	public StaticSprite(BufferedImage sprite, float x, float y) {
		this.sprite = sprite;
		this.x = x;
		this.y = y;
		width = sprite.getWidth();
		height = sprite.getHeight();
	}

	/**
	 * Constructs a new <code>StaticSprite</code> with the specified image.
	 * 
	 * @param sprite <code>BufferedImage</code> for the sprite
	 */
	public StaticSprite(BufferedImage sprite) {
		this(sprite, 0, 0);
	}

	/**
	 * Currently unused by <code>StaticSprite</code> but allows for any type of
	 * <code>Sprite</code> that does require updating to be stored in a list and
	 * updated accordingly.
	 */
	public void update() {
	}

	/**
	 * Draws the <code>BufferedImage</code> to the screen using the
	 * <code>Sprite</code> coordinates and dimensions.
	 * 
	 * @param g2 <code>Graphics2D</code> object to use for rendering
	 */
	public void render(Graphics2D g2) {
		g2.drawImage(sprite, (int) x, (int) y, width, height, null);
	}

	/**
	 * Draws the <code>BufferedImage</code> to the screen at a specified
	 * location using the <code>Sprite</code> dimensions.
	 * 
	 * @param g2 <code>Graphics2D</code> object to use for rendering
	 * @param x location in the x direction to draw the sprite
	 * @param y location in the y direction to draw the sprite
	 */
	public void render(Graphics2D g2, float x, float y) {
		g2.drawImage(sprite, (int) x, (int) y, width, height, null);
	}

	/**
	 * Returns the sprite <code>BufferedImage</code>.
	 * 
	 * @return sprite
	 */
	public BufferedImage getSprite() {
		return sprite;
	}

	/**
	 * Sets the <code>BufferedImage</code> for the sprite.
	 * 
	 * @param sprite new <code>BufferedImage</code> for the sprite
	 */
	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}
}
