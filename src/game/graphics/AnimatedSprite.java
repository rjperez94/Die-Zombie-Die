package game.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * <code>AnimatedSprite</code> is a type of <code>Sprite</code> that holds and
 * displays multiple images to create an animation.
 *
 * @author Brendan Goodenough
 * @version 0.1.0
 */

public class AnimatedSprite extends Sprite {
	private int frame, delay;
	private long timer;
	private BufferedImage[] sprites;

	/**
	 * Constructs a new <code>AnimatedSprite</code> with the specified images
	 * and location.
	 * 
	 * @param sprites <code>BufferedImage[]</code> for the sprites
	 * @param x initial x location
	 * @param y initial y location
	 */
	public AnimatedSprite(BufferedImage[] sprites, float x, float y) {
		this.sprites = sprites;
		this.x = x;
		this.y = y;
		width = sprites[0].getWidth();
		height = sprites[0].getHeight();
		timer = System.currentTimeMillis();
		delay = 80;
	}

	/**
	 * Constructs a new <code>AnimatedSprite</code> with the specified images.
	 * 
	 * @param sprites <code>BufferedImage[]</code> for the sprites
	 */
	public AnimatedSprite(BufferedImage[] sprites) {
		this(sprites, 0, 0);
	}

	/**
	 * Updates the <code>AnimatedSprite</code> by changing the
	 * <code>frame</code> if the specified <code>delay</code> has been met. If
	 * the frame is at the end of the animation it will loop back to the start.
	 */
	public void update() {
		if (System.currentTimeMillis() - timer > delay) {
			frame++;
			timer += delay;
		}

		if (frame >= sprites.length) {
			frame = 0;
		}
	}

	/**
	 * Draws the <code>BufferedImage</code> to the screen using the
	 * <code>Sprite</code> coordinates and dimensions.
	 * 
	 * @param g2 <code>Graphics2D</code> object to use for rendering
	 */
	public void render(Graphics2D g2) {
		g2.drawImage(sprites[frame], (int) x, (int) y, width, height, null);
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
		g2.drawImage(sprites[frame], (int) x, (int) y, width, height, null);
	}

	/**
	 * Returns a <code>BufferedImage</code> array containing all the sprites in
	 * the animation.
	 * 
	 * @return sprites
	 */
	public BufferedImage[] getSprites() {
		return sprites;
	}

	/**
	 * Returns the delay in milliseconds at which frames are changed.
	 * 
	 * @return delay
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Sets the sprites to use for the animation with a
	 * <code>BufferedImage</code> array.
	 * 
	 * @param sprites array of sprites to animate
	 */
	public void setSprites(BufferedImage[] sprites) {
		this.sprites = sprites;
	}

	/**
	 * Sets the delay in milliseconds at which frames are changed.
	 * 
	 * @param delay new delay in milliseconds
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}
}
