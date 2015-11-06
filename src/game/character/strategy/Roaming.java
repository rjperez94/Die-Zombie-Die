package game.character.strategy;

import java.io.Serializable;

import game.Utilities;
import game.Utilities.Direction;
import game.character.Zombie;
import game.world.World;

/**
 * The roaming zombie strategy
 * @author Ronni Perez "perezronn"
 *
 */
public class Roaming implements ZombieState, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5017334766517538744L;
	private Zombie zombie;
	private final float increment;

	/**
	 * Constructor
	 * @param zombie - the zombie this is controlling
	 */
	public Roaming(Zombie zombie) {
		this.zombie = zombie;
		this.increment = 0.8f;
	}

	@Override
	public void move(World world) {

		boolean collided = Utilities.nonMovingCollision(world.getObjects(), this.zombie);	//check for collision

		if (collided) {	//collided
			putBack();
			//change direction and move
			Direction dir = Utilities.randDir(zombie.isFacing());
			zombie.setMoving(true);
			zombie.setFacing(dir);
			moveTo(dir);
		} else {
			zombie.setMoving(true);	//move
			if (zombie.isMoving()) {
				zombie.setFacing(zombie.isFacing());
				moveTo(zombie.isFacing());
			}
		}
	}

	/**
	 * Move this Zombie to given direction
	 * <br>Assume valid move
	 */
	private void moveTo(Direction dir) {
		if (dir==Direction.NORTH) {
			zombie.setY(zombie.getY()-increment);
		} else if (dir==Direction.SOUTH) {
			zombie.setY(zombie.getY()+increment);
		} else if (dir==Direction.WEST) {
			zombie.setX(zombie.getX()-increment);
		} else if (dir == Direction.EAST) {
			zombie.setX(zombie.getX()+increment);
		}
	}

	/**
	 * Move this Zombie to position it is before
	 */
	private void putBack() {
		if (zombie.isFacing() == Direction.NORTH) {
			zombie.setY(zombie.getY()+increment);
		}
		else if (zombie.isFacing() == Direction.SOUTH) {
			zombie.setY(zombie.getY()-increment);
		}
		else if (zombie.isFacing() == Direction.WEST) {
			zombie.setX(zombie.getX()+increment);
		}
		else if (zombie.isFacing() == Direction.EAST) {
			zombie.setX(zombie.getX()-increment);
		}
	}

	// Code Relevant only to loading and saving
	/**
	 * Don't use this constructor should only be used when loading from xml
	 */
	public Roaming(){
		this.increment = 0.8f;
	}

	public Zombie getZombie() {
		return zombie;
	}

	public void setZombie(Zombie zombie) {
		this.zombie = zombie;
	}


}