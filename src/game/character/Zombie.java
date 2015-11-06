package game.character;

import java.io.Serializable;

import game.Utilities.Direction;
import game.character.strategy.*;
import game.exception.GraphicsException;
import game.graphics.Sprite;
import game.graphics.SpriteSheet;
import game.state.PlayState;
import game.world.World;

/**
 * The enemies within the game.
 * @author Ronni Perez "perezronn"
 *
 */
public class Zombie extends Character implements Serializable{
	private static final long serialVersionUID = -1988398104677724289L;
	private ZombieState strategy;	//plan of attack
	private int zombieDamage;		//enemy power

	/**
	 * Constructor
	 * @param name - zombie alias
	 * @param xStart - initial x position
	 * @param yStart - initial y position
	 * @param zStart - initial z position
	 */
	public Zombie(String name, float xStart, float yStart,  float zStart) {
		super(name, xStart, yStart, zStart, 20, null);
		this.zombieDamage = 5;
		this.facing = Direction.SOUTH;
		this.strategy = new Random(this);		//roams around

		loadSprites();
	}

	@Override
	public int damage() {
		return zombieDamage;
	}

	@Override
	public void update(World world, Direction dir) {
		strategy.move(world);

		// update graphics set shown on next render
		for (Sprite sprite : sprites.values()) {
			sprite.update();
		}

	}

	@Override
	public void loadSprites() {
		try {
			SpriteSheet spritesheet = new SpriteSheet("/spritesheets/zombie.png", 4, 8);
			sprites.put("stopDown", spritesheet.getSprite(0, 0));
			sprites.put("down", spritesheet.getSprites(0, 0, 3));
			sprites.put("up", spritesheet.getSprites(1, 0, 3));
			sprites.put("right", spritesheet.getSprites(2, 0, 3));
			sprites.put("left", spritesheet.getSprites(3, 0, 3));
		} catch (GraphicsException e) {
			e.printStackTrace();
		}
	}

	//Load Save Code

	/**
	 * Don't use this constructor should only be used when loading from xml
	 */
	public Zombie() {
		loadSprites();
	}

	public ZombieState getStrategy() {
		return strategy;
	}

	public void setStrategy(ZombieState strategy) {
		this.strategy = strategy;
	}

	public int getZombieDamage() {
		return zombieDamage;
	}

	public void setZombieDamage(int zombieDamage) {
		this.zombieDamage = zombieDamage;
	}


}
