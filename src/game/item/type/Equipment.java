package game.item.type;

import game.graphics.Sprite;

/**
 * This  represents something that a character can carry physically
 * @author Ronni Perez "perezronn"
 *
 */
public abstract class Equipment extends Item {
	private int attack; //the attack increase that the equipment gives
	private int defence; //the defence increase that the equipment gives
	
	/**
	 * Constructor
	 * @param name - equipment alias
	 * @param xStart - initial x position
	 * @param yStart - initial y position
	 * @param zStart - initial z position
	 * @param attackBoost - the increased damage a character will have while wearing this equipment
	 * @param defenceBoost - the increased armor a character will have while wearing this equipment
	 * @param sprite - equipment graphic
	 */
	public Equipment(String name, float xStart, float yStart, float zStart, int attackBoost, int defenceBoost, Sprite sprite) {
		super(name, xStart, yStart, zStart, sprite);
		this.attack = attackBoost;
		this.defence = defenceBoost;
	}
	
	//GETTERS
	/**
	 * @return the attack
	 */
	public int getAttack() {
		return attack;
	}

	/**
	 * @return the defence
	 */
	public int getDefence() {
		return defence;
	}
	
	
	//Load Save Code
	
	/**
	 * Don't use this constructor should only be used when loading from xml
	 */
	public Equipment(){
	}
	
	public void setAttack(int attack){
		this.attack = attack;
	}
	
	public void setDefence(int defence){
		this.defence = defence;
	}
}
