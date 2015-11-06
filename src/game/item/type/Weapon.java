package game.item.type;

import game.graphics.Sprite;

/**
 * A class to represent the Weapon a Player can hold
 * @author Ronni Perez "perezronn"
 *
 */
public abstract class Weapon extends Item {
	protected int damage;	//weapon damage
	
	/**
	 * Constructor
	 * @param name - weapon alias
	 * @param xStart - initial x position
	 * @param yStart - initial y position
	 * @param zStart - initial z position
	 * @param damage -  the weapon damage to a character's health
	 * @param sprite - weapon graphics
	 */
	public Weapon(String name, float xStart, float yStart, float zStart, int damage, Sprite sprite) {
		super(name, xStart, yStart, zStart, sprite);
		this.damage = damage;
	}
	
	//GETTER
	public int getDamage() {
		return damage;
	}
	
	//Load Save Code
	
	/**
	 * Don't use this constructor should only be used when loading from xml
	 */
	public Weapon() {
	}
	
	public void setDamage(int dmg) {
		this.damage = dmg;
	}
}
