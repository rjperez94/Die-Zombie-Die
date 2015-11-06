package game.character.strategy;


import java.io.Serializable;

import game.Utilities;
import game.Utilities.Direction;
import game.character.Zombie;
import game.world.World;

/**
 * The random moving zombie strategy
 * @author Kieran Lewis
 *
 */
public class Random implements ZombieState, Serializable {
	private static final long serialVersionUID = 3645659627760716955L;
	private Zombie zombie;
	private final float increment;
	private long timer;
	private java.util.Random random = new java.util.Random();
	private double range = random.nextInt(3000) + 1000;

	/**
	 * Constructor
	 * @param zombie - the zombie this is controlling
	 */
	public Random(Zombie zombie) {
		this.zombie = zombie;
		this.increment = 0.8f;
		this.timer = System.currentTimeMillis();
	}

	@Override
	public void move(World world) {

		boolean collided = Utilities.nonMovingCollision(world.getObjects(), this.zombie);	//check for collision
		long delta = 0;

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
				//change direction if the change in time is greater than the range
				delta = System.currentTimeMillis();
				if(delta - timer >= range) {
					Direction dir = Utilities.randDir(zombie.isFacing());
					zombie.setFacing(dir);
					moveTo(dir);
					timer = delta;
				}

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
	public Random(){
		this.increment = 0.8f;
	}

	public Zombie getZombie() {
		return zombie;
	}

	public void setZombie(Zombie zombie) {
		this.zombie = zombie;
	}


}