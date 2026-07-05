package game.graphics;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;

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
	 * @param filepath path to the file relative to <code>Game</code>
	 * @return <code>BufferedImage</code> for the requested image
     */
	public static BufferedImage loadImage(String filepath) throws GraphicsException {
		try {
			return ImageIO.read(new File(filepath));
		} catch (Exception e) {
			throw new GraphicsException("Unable to load " + filepath);
		}
	}

	/**
	 * Returns a <code>Font</code> that is loaded from the specified file.
	 * 
	 * @param filepath path to the file relative to <code>Game</code>
	 * @return <code>Font</code> for the requested font file
     */
	public static Font loadFont(String filepath) throws GraphicsException {
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File(filepath));
			font = font.deriveFont(12f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(font);
			return font;
		} catch (Exception e) {
			throw new GraphicsException("Unable to load " + filepath);
		}
	}
}
