package game.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import game.character.Player;
import game.graphics.Sprite;
import game.item.type.Item;

/**
 * This represents the SupplyBox. It can hold unlimited number of items
 * @author Ronni Perez "perezronn"
 *
 */
public class SupplyBox extends Item implements Serializable  {
	/**
	 *
	 */
	private static final long serialVersionUID = 5349765482284628730L;
	private List<Item> items;

	/**
	 * Constructor
	 * @param name - supplyBox alias
	 * @param xStart - initial x position
	 * @param yStart - initial y position
	 * @param zStart - initial z position
	 * @param sprite - item graphics
	 */
	public SupplyBox(String name, float xStart, float yStart, float zStart, Sprite sprite) {
		super(name, xStart, yStart, zStart, sprite);
		this.items = new ArrayList<Item>();
	}

	/**
	 * Game adds an Item to the List of items
	 * @param it - Item to be added
	 */
	public void putIn(Item it) {
		items.add(it);
	}

	/**
	 * Player removes an Item at index
	 * @param pl - Player who opened supply box
	 * @param index - index of item to get and remove
	 */
	public void getOut(Player pl, int index) {
		if (index < 0 || index >= items.size()) {	//check for bounds
			return;
		}

		Item item = items.get(index);
		if (pl.addToInventory(item)) {	//if add successful
			items.remove(item);	//remove from here
		}
	}

	@Override
	public void use(Player player) {
		while (!items.isEmpty()) {
			Item item = items.get(0);
			if (player.addToInventory(item)) {
				items.remove(item);
			} else {
				return;
			}
		}
	}

}
