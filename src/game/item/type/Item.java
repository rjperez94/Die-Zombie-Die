package game.item.type;

import game.character.Player;
import game.graphics.Sprite;
import game.world.GameObject;

/**
 * A class for all the items including furniture, chests (containers) and moveable items (anything you can pick up) 
 * @author Ronni Perez "perezronn"
 *
 */
public abstract class Item extends GameObject {
	/**
	 * Constructor
	 * @param name - item alias
	 * @param xStart - initial x position
	 * @param yStart - initial y position
	 * @param zStart - initial z position
	 * @param sprite - item graphics
	 */
	public Item(String name, float xStart, float yStart, float zStart, Sprite sprite) {
		super(name, xStart, yStart, zStart, sprite);
	}
	
	/**
	 * Interact with this item
	 * @param player - the Player using this item
	 */
	public abstract void use (Player player);
	
	
	
	//Load Save Code only
	/**
	 * Don't use this constructor should only be used when loading from xml
	 */
	public Item(){
		
	}
}
