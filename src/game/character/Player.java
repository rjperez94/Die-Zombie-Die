package game.character;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import game.Game;
import game.Utilities;
import game.Utilities.Direction;
import game.exception.GraphicsException;
import game.graphics.Sprite;
import game.graphics.SpriteSheet;
import game.item.KeyCard;
import game.item.type.Item;
import game.item.type.Weapon;
import game.state.PlayState;
import game.world.World;

/**
 * Represents the character that the player controls
 *
 * @author Ronni Perez "perezronn"
 *
 */
public class Player extends Character implements Serializable {
	private static final long serialVersionUID = -6350855470010903301L;
	public static final double ZOMBIE_DAMAGE = 0.5;
	public static final float PLAYER_DAMAGE = 1;

	public final long UID = System.currentTimeMillis();


	// world offset
	private float xOffset, yOffset;

	private final int inventorySize = 10; // backpack size
	private List<Item> inventory; // backpack
	private int currentItem; // holding current item
	private int kills;
	private int bullets; // bullets this player has

	/**
	 * Constructor
	 *
	 * @param name
	 *            - player alias
	 * @param xStart
	 *            - initial x position
	 * @param yStart
	 *            - initial y position
	 * @param zStart
	 *            - initial z position
	 */
	public Player(String name, float xStart, float yStart, float zStart) {
		super(name, xStart, yStart, zStart, 100, null); // 100 health
		this.xOffset = 0;
		this.yOffset = 0;

		this.kills = 0;
		this.bullets = 0;
		this.inventory = new ArrayList<Item>();
		this.currentItem = -1; // hold NO item by default

		this.facing = Direction.SOUTH; // facing south, speed 1
		// load graphics
		loadSprites();
		adjustOffsets();
	}

	/**
	 * Adjust game level offset values accordingly for proper player render
	 */
	private void adjustOffsets() {
		int centerX = Game.WIDTH / 2 - 8; // intended game window center
		int centerY = Game.HEIGHT / 2 - 14;

		// adjust x offset - only one of the two while loops will be performed
		while (centerX < xLoc) { // adjust xOff going east
			xOffset -= velocity;
			centerX++;
		}
		while (centerX > xLoc) { // adjust xOff going west
			xOffset += velocity;
			centerX--;
		}

		// adjust y offset - only one of the two while loops will be performed
		while (centerY < yLoc) { // adjust yOff going south
			yOffset -= velocity;
			centerY++;
		}
		while (centerY > yLoc) { // adjust yOff going north
			yOffset += velocity;
			centerY--;
		}
	}

	/**
	 * Load animated graphics set for player
	 */
	public void loadSprites() {
		try {
			SpriteSheet spritesheet = new SpriteSheet(
					"/spritesheets/player.png", 4, 8);
			sprites.put("stopDown", spritesheet.getSprite(0, 0));
			sprites.put("stopUp", spritesheet.getSprite(1, 0));
			sprites.put("stopRight", spritesheet.getSprite(2, 0));
			sprites.put("stopLeft", spritesheet.getSprite(3, 0));
			sprites.put("down", spritesheet.getSprites(0, 0, 3));
			sprites.put("up", spritesheet.getSprites(1, 0, 3));
			sprites.put("right", spritesheet.getSprites(2, 0, 3));
			sprites.put("left", spritesheet.getSprites(3, 0, 3));
		} catch (GraphicsException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Increases the characters health by the amount they were boosted
	 *
	 * @param boost
	 *            - amount of health points added to character health
	 */
	public void replenish(int boost) {
		health = health + boost;
	}

	@Override
	public int damage() {
		Item item = inventory.get(currentItem);
		if (item instanceof Weapon) { // must currently hold a weapon
			Weapon weapon = (Weapon) item;
			return weapon.getDamage();
		}
		// not a weapon
		return 0;
	}

	/**
	 * Add Item to Player's inventory
	 *
	 * @param it
	 *            - Item being added
	 * @return true iff the Item is successfully added
	 */
	public boolean addToInventory(Item it) {
		if (inventory.size() <= inventorySize) { // check limit
			return inventory.add(it);
		}
		// over the limit
		return false;
	}

	/**
	 * Removes Item at currentItem index <br>
	 * Assume valid index
	 */
	public void removeCurrent() {
		inventory.remove(currentItem);
	}

	/**
	 * @return a String representation of the Player's inventory <br>
	 *         for pretty printing
	 */
	public String showInventory() {
		StringBuilder text = new StringBuilder();
		if (inventory.isEmpty()) {
			text.append("You nothing in your inventory");
			return text.toString();
		}

		if (hasKey()) {
			text.append("You have a key.You have access to the locked Door  \n");
		}

		text.append("You have the following items: \n");
		for (int i = 0; i < inventory.size(); i++) { // output each on one line
			Item item = inventory.get(i);
			if (!(item instanceof KeyCard)) {
				text.append("PRESS " + i + " to select " + item.toString()
						+ "\n");
			} else {
				text.append("The item at "
						+ i
						+ " is a key and can be used automatically by pressing E near a locked Door \n");
			}
		}
		text.append("Then press SHIFT KEY to use it");
		return text.toString();
	}

	/**
	 * Check if an Item is in a Player's back pack
	 *
	 * @param it
	 *            - Item to be checked
	 * @return true iff Item is in Player's inventory
	 */
	public boolean hasItem(Item it) {
		if (it == null) { // item to find cannot be null
			return false;
		}
		for (int i = 0; i < inventory.size(); i++) { // check each item
			if (it.equals(inventory.get(i))) {
				return true;
			}
		}
		// non-existent
		return false;
	}

	/**
	 * Returns true iff this Player has a KeyCard in its inventory
	 *
	 * @return
	 */
	public boolean hasKey() {
		for (Item i : inventory) {
			if (i instanceof KeyCard) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void update(World world, Direction dir) {
		if (dir != null) { // move
			moving = true;
			facing = dir;
			moveTo(world, dir);
		} else { // stationary
			moving = false;
		}

		takeItem(world);
		collide(world, true);

		// update graphics set shown on next render
		for (Sprite sprite : sprites.values()) {
			sprite.update();
		}
	}

	public Character collide(World world, boolean isSinglePlayer) {
		// for zombie/other players collisions
		Character collide = Utilities
				.movingCollision(world.getEntities(), this); // check
		if (isSinglePlayer) {
			if (collide != null) { // decrease health
				if (collide instanceof Zombie) {
					this.health -= ZOMBIE_DAMAGE;
				} else {
					this.health -= PLAYER_DAMAGE;
				}
			}
		}
		return collide;
	}

	public Item takeItem(World world) {
		// check for pickableItems collision
		Item taken = Utilities.takeItem(world.getPickable(), this);
		if (taken != null) {
			world.takeItem(taken, "floor");
		}
		return taken;
	}

	/**
	 * Move this Player to given direction <br>
	 * Check for valid move
	 *
	 * @param state
	 *            - the game level
	 * @param world
	 * @param dir
	 *            - Direction to move to
	 * @param xOffset
	 *            - x coordinate centre offset
	 * @param yOffset
	 *            - y coordinate centre offset
	 * @return true iff move to direction is a non-collision move
	 */
	public boolean moveTo(World world, Direction dir) {
		if (dir == Direction.NORTH) {
			boolean collided = Utilities.nonMovingCollision(world.getObjects(),
					new Player(null, xLoc, yLoc - velocity, zLoc));
			if (!collided) {
				yOffset += velocity;
				yLoc -= velocity;
				return true;
			}
		} else if (dir == Direction.SOUTH) {
			boolean collided = Utilities.nonMovingCollision(world.getObjects(),
					new Player(null, xLoc, yLoc + velocity, zLoc));
			if (!collided) {
				yOffset -= velocity;
				yLoc += velocity;
				return true;
			}
		} else if (dir == Direction.WEST) {
			boolean collided = Utilities.nonMovingCollision(world.getObjects(),
					new Player(null, xLoc - velocity, yLoc, zLoc));
			if (!collided) {
				xOffset += velocity;
				xLoc -= velocity;
				return true;
			}
		} else if (dir == Direction.EAST) {
			boolean collided = Utilities.nonMovingCollision(world.getObjects(),
					new Player(null, xLoc + velocity, yLoc, zLoc));
			if (!collided) {
				xOffset -= velocity;
				xLoc += velocity;
				return true;
			}
		}
		return false;
	}

	/**
	 * Renders the animated sprite to the screen
	 *
	 * @param world
	 * @param g2
	 *            - the graphics object
	 */
	public void render(Graphics2D g2) {
		if (!moving && facing == Direction.SOUTH) {
			sprites.get("stopDown").render(g2, Game.WIDTH / 2 - 8,
					Game.HEIGHT / 2 - 14);
		} else if (!moving && facing == Direction.NORTH) {
			sprites.get("stopUp").render(g2, Game.WIDTH / 2 - 8,
					Game.HEIGHT / 2 - 14);
		} else if (!moving && facing == Direction.EAST) {
			sprites.get("stopRight").render(g2, Game.WIDTH / 2 - 8,
					Game.HEIGHT / 2 - 14);
		} else if (!moving && facing == Direction.WEST) {
			sprites.get("stopLeft").render(g2, Game.WIDTH / 2 - 8,
					Game.HEIGHT / 2 - 14);
		} else if (facing == Direction.SOUTH) {
			sprites.get("down").render(g2, Game.WIDTH / 2 - 8,
					Game.HEIGHT / 2 - 14);
		} else if (facing == Direction.NORTH) {
			sprites.get("up").render(g2, Game.WIDTH / 2 - 8,
					Game.HEIGHT / 2 - 14);
		} else if (facing == Direction.EAST) {
			sprites.get("right").render(g2, Game.WIDTH / 2 - 8,
					Game.HEIGHT / 2 - 14);
		} else if (facing == Direction.WEST) {
			sprites.get("left").render(g2, Game.WIDTH / 2 - 8,
					Game.HEIGHT / 2 - 14);
		}
	}

	/**
	 * Decrease player's bullets by 1
	 */
	public void decrementBullets() {
		bullets--;
	}

	/**
	 * Increase player's bullets by 1
	 *
	 * @param increaseBy
	 *            -- increase player bullets by this number
	 */
	public void incrementBullets(int increaseBy) {
		bullets += increaseBy;
	}

	/**
	 * Add one to kills tally
	 */
	public void incrementKills() {
		kills++;
	}

	// GETTERS/SETTERS
	public void setCurrentItem(int setTo) {
		if (setTo >= 0 && setTo < inventory.size()) { // check bounds
			currentItem = setTo;
		} else {
			currentItem = -1;
		}
	}

	public Item getItemAtHand() {
		if (!inventory.isEmpty()) {
			// check for bounds
			if (currentItem >= 0 && currentItem < inventory.size()) {
				Item i = inventory.get(currentItem);
				i.setX(this.xLoc);
				i.setY(this.yLoc);
				i.setZ(this.zLoc);
				return inventory.get(currentItem);
			}

		}
		// empty backpack
		return null;
	}

	/**
	 * Get a List of all items in player's back pack
	 *
	 * @return
	 */
	public List<Item> getInventory() {
		return inventory;
	}

	public Direction isFacing() {
		return facing;
	}

	public void setFacing(Direction newDir) {
		facing = newDir;
	}

	public int getKills() {
		return kills;
	}

	public int getBullets() {
		return bullets;
	}

	// Code Relevant only to loading and saving
	/**
	 * Don't use this constructor should only be used when loading from xml
	 */
	public Player() {

	}

	public int getCurrentItem() {
		return currentItem;
	}

	public void setInventory(List<Item> inventory) {
		this.inventory = inventory;
	}

	public void setBullets(int bullets) {
		this.bullets = bullets;
	}

	public float getXOffset() {
		return xOffset;
	}

	public float getYOffset() {
		return yOffset;
	}

	public void changeXOffsetTo(float xOffset) {
		this.xOffset = xOffset;
	}

	public void changeYOffsetTo(float yOffset) {
		this.yOffset = yOffset;
	}
}
