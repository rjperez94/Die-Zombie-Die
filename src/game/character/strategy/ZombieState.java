package game.character.strategy;

import game.world.World;

/**
 * The enemy's plan of attack. This ensures that every zombie has a move algorithm
 * @author Ronni Perez 'perezronn'
 *
 */
public interface ZombieState {
	/**
	 * Updates the enemy's position appropriately
	 * @param map - the world map
	 */
	public void move(World map);
}
