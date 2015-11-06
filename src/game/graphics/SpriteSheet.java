package game.graphics;

import java.awt.image.BufferedImage;

import game.exception.GraphicsException;

/**
 * <code>SpriteSheet</code> is designed to divide an sprite sheet into sub
 * images objects and store them in a two dimensional <code>BufferedImage</code>
 * array.
 * 
 * @author Brendan Goodenough
 * @version 0.1.0
 */

public class SpriteSheet {
	private int rows, cols;
	private BufferedImage spritesheet;
	private BufferedImage[][] sprites;

	/**
	 * Constructs a new <code>SpriteSheet</code> by loading the image of the
	 * specified <code>filename</code> and dividing it into sub images based on
	 * the specified <code>rows</code> and <code>cols</code>.
	 * 
	 * @param filename path to the desired image
	 * @param rows number of rows in the spritesheet
	 * @param cols number of columns in the spritesheet
	 * @throws GraphicsException
	 */
	public SpriteSheet(String filename, int rows, int cols) throws GraphicsException {
		this.rows = rows;
		this.cols = cols;
		sprites = new BufferedImage[rows][cols];

		try {
			spritesheet = GraphicsLoader.loadImage(filename);
		} catch (GraphicsException e) {
			throw new GraphicsException("Error: Unable to create spritesheet");
		}

		int width = spritesheet.getWidth() / cols;
		int height = spritesheet.getHeight() / rows;

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int x = width * col;
				int y = height * row;
				sprites[row][col] = spritesheet.getSubimage(x, y, width, height);
			}
		}
	}

	/**
	 * Returns a <code>StaticSprite</code> using the <code>BufferedImage</code>
	 * at the specified <code>row</code> and <code>col</code> in the
	 * <code>sprites</code> array.
	 * 
	 * @param row row of the desired sprite
	 * @param col column of the desired sprite
	 * @return <code>StaticSprite</code> containing the desired image
	 * @throws GraphicsException
	 */
	public StaticSprite getSprite(int row, int col) throws GraphicsException {
		if (row >= rows || col >= cols) {
			throw new GraphicsException("Error: Specified row or column is out of bounds");
		}

		StaticSprite sprite = new StaticSprite(sprites[row][col]);
		return sprite;
	}

	/**
	 * Returns an <code>AnimatedSprite</code> with a
	 * <code>BufferedImage[]</code> containing each sprite in the
	 * <code>sprites</code> array that is within the specified region.
	 * 
	 * @param row row of the desired sprites
	 * @param colStart starting column of the desired sprites
	 * @param colEnd end column of the desired sprites
	 * @return <code>AnimatedSprite</code> containing the desired frames
	 * @throws GraphicsException
	 */
	public AnimatedSprite getSprites(int row, int colStart, int colEnd) throws GraphicsException {
		if (colStart < 0 || colEnd >= cols) {
			throw new GraphicsException("Error: Specified columns are out of bounds");
		} else if (row < 0 || row >= rows) {
			throw new GraphicsException("Error: Specified row is out of bounds");
		}

		try {
			int size = colEnd - colStart + 1;
			BufferedImage[] sprites = new BufferedImage[size];

			for (int col = colStart, i = 0; col <= colEnd; col++) {
				sprites[i++] = this.sprites[row][col];
			}

			AnimatedSprite sprite = new AnimatedSprite(sprites);
			return sprite;
		} catch (Exception e) {
			throw new GraphicsException("Error: Unable to create the AnimatedSprite");
		}
	}
}
