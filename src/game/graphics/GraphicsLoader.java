package game.graphics;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import game.Game;
import game.exception.GraphicsException;

/**
 * <code>GraphicsLoader</code> contains several helper methods that can be used
 * to easily load graphics and fonts.
 * 
 * @author Brendan Goodenough
 * @version 0.1.0
 */

public class GraphicsLoader {
	/**
	 * Returns a <code>BufferedImage</code> that is loaded from the specified
	 * file.
	 * 
	 * @param filename path to the file relative to <code>Game</code>
	 * @return <code>BufferedImage</code> for the requested image
	 * @throws <code>GraphicsException</code>
	 */
	public static BufferedImage loadImage(String filepath) throws GraphicsException {
		try {
			return ImageIO.read(Game.class.getResourceAsStream(filepath));
		} catch (Exception e) {
			throw new GraphicsException("Unable to load " + filepath);
		}
	}

	/**
	 * Returns a <code>Font</code> that is loaded from the specified file.
	 * 
	 * @param filename path to the file relative to <code>Game</code>
	 * @return <code>Font</code> for the requested font file
	 * @throws <code>GraphicsException</code>
	 */
	public static Font loadFont(String filepath) throws GraphicsException {
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, Game.class.getResourceAsStream(filepath));
			font = font.deriveFont(12f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(font);
			return font;
		} catch (Exception e) {
			throw new GraphicsException("Unable to load " + filepath);
		}
	}
}
