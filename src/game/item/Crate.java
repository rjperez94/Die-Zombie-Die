package game.item;

import java.io.Serializable;
import java.util.List;

import game.Utilities.Direction;
import game.character.Player;
import game.graphics.Sprite;
import game.item.type.Item;
import game.world.Floor;
import game.world.GameObject;
import game.world.World;

/**
 * This represents a Crate (box). It can be movable or stationary
 * @author Ronni Perez "perezronn"
 * @author Kieran Lewis
 *
 */
public class Crate extends Item implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 6861153714450365228L;
	private final boolean isMovable;	//locomotive, stationary
	/**
	 * Constructor
	 * @param name - crate alias
	 * @param xStart - initial x position
	 * @param yStart - initial y position
	 * @param zStart - initial z position
	 * @param movable - true iff crate can be moved
	 * @param sprite - item graphics
	 */
	public Crate (String name, float xStart, float yStart, float zStart, boolean movable, Sprite sprite) {
		super(name, xStart, yStart, zStart, sprite);
		this.isMovable = movable;
	}

	@Override
	public void use(Player player) {
		//NOT IMPLEMENTTED
	}

	/**
	 * @return the isMovable
	 */
	public boolean isMovable() {
		return isMovable;
	}

	/**
	 * Interact with this item (SPECIAL CODE HANDLING)
	 * @param world - the world map
	 * @param player - the Player using this item
	 * @param floorSprite - the Floor graphics
	 */
	public void use(World world, Player player, Sprite floorSprite) {
		if (!isMovable) return;		//check type

		if (player.isFacing() == Direction.EAST) { //player from WEST of item
			if (!crateCollision(world.getObjects(), new Crate(name, xLoc+1, yLoc, zLoc, isMovable, null))) {
				return;
			}
			setToFloor(world,floorSprite);
			this.xLoc+=1;
			setToThis(world);
		} else if (player.isFacing() == Direction.WEST) { //player from EAST of item
			if (!crateCollision(world.getObjects(), new Crate(name, xLoc-1, yLoc, zLoc, isMovable, null))) {
				return;
			}
			setToFloor(world, floorSprite);
			this.xLoc-=1;
			setToThis(world);
		} else if (player.isFacing() == Direction.SOUTH) { //player from NORTH of item
			if (!crateCollision(world.getObjects(), new Crate(name, xLoc, yLoc+1, zLoc, isMovable, null))) {
				return;
			}
			setToFloor(world, floorSprite);
			this.yLoc+=1;
			setToThis(world);
		} else if (player.isFacing() == Direction.NORTH) { //player from SOUTH of item
			if (!crateCollision(world.getObjects(), new Crate(name, xLoc, yLoc-1, zLoc, isMovable, null))) {
				return;
			}
			setToFloor(world, floorSprite);
			this.yLoc-=1;
			setToThis(world);
		}
	}

	//HELPER METHODS
	/**
	 * Set coordinate at this NEW position as this Crate
	 * @param world - the world map
	 */
	private void setToThis(World world) {
		world.setObject((int)yLoc, (int)xLoc, this);
	}

	/**
	 * Set coordinate at this OLD position as a Floor
	 * @param world - the world map
	 * @param sprite - the Floor graphics
	 */
	private void setToFloor(World world, Sprite sprite) {
		world.setObject((int)yLoc, (int)xLoc, new  Floor("Floor at :"+yLoc+" "+xLoc, xLoc, yLoc, 0, sprite));
	}

	/**
	 * Returns true iff this crate's boundingBox intersects an object's
	 * boundingBox
	 *
	 * @param list - List of stationary objects in the world map
	 * @param crate
	 * @return
	 */
	private boolean crateCollision(List<GameObject> list, Crate crate) {
		for (GameObject o : list) {
			if (o.boundingBox().intersects(crate.boundingBox())) { // collision
				return true;
			}
		}
		return false;
	}

}
