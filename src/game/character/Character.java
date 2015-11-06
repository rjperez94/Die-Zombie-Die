package game.character;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import javafx.geometry.BoundingBox;
import game.Utilities.Direction;
import game.graphics.Sprite;
import game.state.PlayState;
import game.world.GameObject;
import game.world.World;

/**
 * Represents the various game characters within the game
 * @author Ronni Perez "perezronn"
 *
 */
public abstract class Character extends GameObject {
	protected float health;
	protected boolean moving;		//stationary, locomotive

	protected final float velocity;		//direction and speed
	protected Direction facing;

	protected Map<String,Sprite> sprites;	//graphics set for a given character

	/**
	 * The Constructor
	 * @param name - character alias
	 * @param xStart - initial x position
	 * @param yStart - initial y position
	 * @param zStart - initial z position
	 * @param health - base health
	 */
	public Character(String name, float xStart, float yStart, float zStart,  int health, Sprite sprite) {
		super(name, xStart, yStart, zStart, sprite);
		this.velocity = 1f;
		this.sprites = new HashMap<String,Sprite>();
		this.health = health;
	}

	/**
	 * Decreases the characters health by the amount they were damaged
	 * @param zombieDamage		amount of damage dealt to character
	 */
	public void hurt(double zombieDamage){
		health = (float) (health - zombieDamage);
		if(health<0){
			health = 0;
		}
	}

	@Override
	public BoundingBox boundingBox() {
		return new BoundingBox(xLoc+2, yLoc+(getFeetHeight()*3), getFeetWidth(), getFeetHeight());
	}

	/**
	 * Returns the entire area of the Bounding Box occupied by this character
	 * @return
	 */
	public BoundingBox entireBoundingBox() {
		return new BoundingBox(xLoc, yLoc, World.TILE_SIZE, World.TILE_SIZE*1.7);
	}

	@Override
	public void render(Graphics2D g2, float xOffset, float yOffset) {
		if (facing == Direction.SOUTH) {
			sprites.get("down").render(g2, xOffset + xLoc, yOffset + yLoc);
		} else if (facing == Direction.NORTH) {
			sprites.get("up").render(g2, xOffset + xLoc, yOffset + yLoc);
		} else if (facing == Direction.EAST) {
			sprites.get("right").render(g2, xOffset + xLoc, yOffset + yLoc);
		} else if (facing == Direction.WEST) {
			sprites.get("left").render(g2, xOffset + xLoc, yOffset + yLoc);
		}
	}

	/**
	 * Width of a character's feet from left foot to right foot
	 * @return
	 */
	public int getFeetWidth() {
		return 12;
	}

	/**
	 * Height between a character's waist to tip of foot
	 * @return
	 */
	public int getFeetHeight() {
		return 6;
	}


	/**
	 * Returns the damage output of a character
	 * @return
	 */
	public abstract int damage();

	/**
	 * Updates a game object's state in a given tick
	 * @param world - this world map
	 * @param dir - Direction this character is traveling to
	 */
	public abstract void update(World world, Direction dir);

	/**
	 * Loads the characters sprites from file
	 */
	public abstract void loadSprites();

	//GETTTERS/SETTERS
	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public Direction isFacing() {
		return facing;
	}

	public void setFacing(Direction facing) {
		this.facing = facing;
	}

	public float getVelocity() {
		return velocity;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	//Load Save Only
	/**
	 * Don't use this constructor should only be used when loading from xml
	 */
	public Character(){
		this.velocity = 1f;
		this.sprites = new HashMap<String,Sprite>();

	}

	public Direction getFacing() {
		return facing;
	}
}
