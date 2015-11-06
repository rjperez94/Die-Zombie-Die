package game.item;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

import game.Utilities;
import game.character.Character;
import game.character.Player;
import game.item.type.Equipment;
import game.world.World;

/**
 * This represents Sonic Power.
 * <br>This damages characters in surrounding diameter area
 * @author Ronni Perez "perezronn"
 *
 */
public class SonicPower extends Equipment implements Serializable  {
	/**
	 *
	 */
	private static final long serialVersionUID = 777268751540305846L;
	private int diameter;

	/**
	 * Constructor
	 * @param name - equipment alias
	 * @param xStart - initial x position
	 * @param yStart - initial y position
	 * @param zStart - initial z position
	 */
	public SonicPower(String name, float xStart, float yStart, float zStart) {
		super(name, xStart, yStart, zStart, 15, 0, null);
		this.diameter = 75;
	}

	@Override
	public void use(Player player) {
		// NOT IMPLEMENTED
	}

	/**
	 * Interact with this item (SPECIAL CODE HANDLING)
	 *
	 * @param world
	 *            - the world map
	 * @param player
	 *            - the Player using this item
	 */
	public void use(World world, Player player) {
		// for each character in game
		for (Character c : world.getEntities()) {
			// damage area radius
			if (Utilities.distance(player, c) < diameter/2) {
				// decrease health
				c.hurt(getAttack());
			}
		}
	}

	@Override
	public void render(Graphics2D g2, float xOffset, float yOffset) {
		double realX = xOffset + (xLoc+(World.TILE_SIZE/2));
		double realY = yOffset + (yLoc+(World.TILE_SIZE/0.8));

		int x = (int) (realX - (diameter / 2));
		int y = (int) (realY - (diameter / 2));
		g2.setColor(new Color(0,0,255,100));
		g2.fillOval(x, y, diameter, diameter);
	}

	//Load Save Code

	/**
	 * Don't use this constructor should only be used when loading from xml
	 */
	public SonicPower(){

	}

	public int getDiameter() {
		return diameter;
	}

	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}


}
