import static org.junit.Assert.*;

import org.junit.Test;

import game.exception.GraphicsException;
import game.graphics.AnimatedSprite;
import game.graphics.GraphicsLoader;
import game.graphics.SpriteSheet;
import game.graphics.StaticSprite;

/**
 * GraphicsTests are designed to test all classes within the game.graphics
 * package.
 */

public class GraphicsTests {
	@Test
	public void testLoadValidImage() {
		try {
			GraphicsLoader.loadImage("/backgrounds/menu.png");
		} catch (GraphicsException e) {
			fail("Should load image");
		}
	}

	@Test
	public void testLoadInvalidImage() {
		try {
			GraphicsLoader.loadImage("wassup");
			fail("Should not load image");
		} catch (GraphicsException ignored) {
		}
	}

	@Test
	public void testLoadValidFont() {
		try {
			GraphicsLoader.loadFont("/fonts/gamegirl.ttf");
		} catch (GraphicsException e) {
			fail("Should load font");
		}
	}

	@Test
	public void testLoadInvalidFont() {
		try {
			GraphicsLoader.loadFont("wassup");
			fail("Should not load font");
		} catch (GraphicsException ignored) {
		}
	}

	@Test
	public void testCreateValidSpriteSheet() {
		try {
			new SpriteSheet("/backgrounds/menu.png", 2, 2);
		} catch (GraphicsException e) {
			fail("Should create valid spritesheet");
		}
	}

	@Test
	public void testCreateInvalidSpriteSheet() {
		try {
			new SpriteSheet(null, 2, 2);
			fail("Should create valid static sprite");
		} catch (GraphicsException ignored) {
		}
	}

	@Test
	public void testCreateValidStaticSprite() {
		try {
			SpriteSheet spritesheet = new SpriteSheet("/backgrounds/menu.png", 2, 2);
			spritesheet.getSprite(0, 0);
		} catch (GraphicsException e) {
			fail("Should create valid static sprite");
		}
	}

	@Test
	public void testCreateInvalidStaticSprite1() {
		try {
			SpriteSheet spritesheet = new SpriteSheet("/backgrounds/menu.png", 2, 2);
			spritesheet.getSprite(2, 2);
			fail("Should not create a static sprite");
		} catch (GraphicsException ignored) {
		}
	}

	@Test
	public void testCreateInvalidStaticSprite2() {
		try {
			new StaticSprite(null);
			fail("Should not create a static sprite");
		} catch (Exception ignored) {
		}
	}

	@Test
	public void testCreateValidAnimatedSprite1() {
		try {
			SpriteSheet spritesheet = new SpriteSheet("/backgrounds/menu.png", 2, 2);
			spritesheet.getSprites(0, 0, 1);
		} catch (GraphicsException e) {
			fail("Should create valid static sprite");
		}
	}

	@Test
	public void testCreateValidAnimatedSprite2() {
		try {
			SpriteSheet spritesheet = new SpriteSheet("/backgrounds/menu.png", 2, 2);
			AnimatedSprite sprite = spritesheet.getSprites(0, 0, 1);
            assertEquals(2, sprite.getSprites().length);
		} catch (GraphicsException e) {
			fail("Should create valid static sprite");
		}
	}

	@Test
	public void testCreateValidAnimatedSprite3() {
		try {
			SpriteSheet spritesheet = new SpriteSheet("/backgrounds/menu.png", 2, 2);
			AnimatedSprite sprite = spritesheet.getSprites(0, 0, 1);
            assertEquals(2, sprite.getSprites().length);
		} catch (GraphicsException e) {
			e.printStackTrace();
			fail("Should create valid static sprite");
		}
	}

	@Test
	public void testCreateInvalidAnimatedSprite1() {
		try {
			SpriteSheet spritesheet = new SpriteSheet("/backgrounds/menu.png", 2, 2);
			spritesheet.getSprites(0, 0, 2);
			fail("Should not create valid static sprite");
		} catch (GraphicsException ignored) {
		}
	}

	@Test
	public void testCreateInvalidAnimatedSprite2() {
		try {
			SpriteSheet spritesheet = new SpriteSheet("/backgrounds/menu.png", 2, 2);
			spritesheet.getSprites(0, 2, 0);
			fail("Should not create valid static sprite");
		} catch (GraphicsException ignored) {
		}
	}
}
