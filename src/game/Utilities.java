package game;

import java.util.List;
import java.util.Map;
import java.util.Random;

import game.character.Character;
import game.character.Player;
import game.character.Zombie;
import game.graphics.Sprite;
import game.item.KeyCard;
import game.item.Phaser;
import game.item.Pill;
import game.item.ShieldOfAres;
import game.item.SonicPower;
import game.item.SupplyBox;
import game.item.Vest;
import game.item.type.Item;
import game.world.Bullet;
import game.world.GameObject;
import game.world.World;

/**
 * Utilities contains helper methods which aid the game logic aspect
 * 
 * @author Ronni Perez "perezronn"
 *
 */
public class Utilities {

	/**
	 * Return distance between Player and GameObject
	 * 
	 * @param p - the Player
	 * @param o - the GameObject (may be another Character OR Item)
	 * @return
	 */
	public static double distance(Player p, GameObject o) {
		if (o instanceof Character) {
			return Math.hypot((p.getX() + (World.TILE_SIZE / 2)) - (o.getX() + (World.TILE_SIZE / 2)),
					(p.getY() + (World.TILE_SIZE / 2)) - (o.getY() + (World.TILE_SIZE / 2)));
		} else { // else, Item
			return Math.hypot(p.getX() + (World.TILE_SIZE / 4) - (o.getX() * World.TILE_SIZE) + (World.TILE_SIZE / 4),
					p.getY() + ((World.TILE_SIZE / 4) * 3) - (o.getY() * World.TILE_SIZE) + (World.TILE_SIZE / 4));
		}
	}

	/**
	 * Returns a randomly generated integer between min to max (inclusive)
	 * 
	 * @param - min integer lower bound
	 * @param - max integer upper bound
	 * @return
	 */
	public static int randNum(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	/**
	 * Returns a randomly generated Direction except exempted
	 * 
	 * @param exempted - this Direction should NOT be chosen
	 * @return
	 */
	public static Direction randDir(Direction exempted) {
		while (true) {
			int choice = randNum(1, 4);
			if (choice == 1) {
				if (exempted != Direction.NORTH) {
					return Direction.NORTH;
				}
			} else if (choice == 2) {
				if (exempted != Direction.SOUTH) {
					return Direction.SOUTH;
				}
			} else if (choice == 3) {
				if (exempted != Direction.WEST) {
					return Direction.WEST;
				}
			} else {
				if (exempted != Direction.EAST) {
					return Direction.EAST;
				}
			}
		}
	}

	/**
	 * Returns a randomly generated boolean
	 * 
	 * @return
	 */
	public static boolean randBoolean() {
		return new Random().nextBoolean();
	}

	/**
	 * Returns a randomly generated Item and to be put in a SupplyBox (done
	 * elsewhere)
	 * 
	 * @param sb - the SupplyBox to put this Item into
	 * @param sprites - the graphics set in the game
	 * @return
	 */
	public static Item randItem(SupplyBox sb, Map<String, Sprite> sprites) {
		int choice = randNum(1, 6);
		if (choice == 1) {
			return new KeyCard("KeyCard at: " + sb.getY() + " " + sb.getX(), sb.getX(), sb.getY(), 700,
					sprites.get("key"));
		} else if (choice == 2) {
			return new Phaser("Phaser at: " + sb.getY() + " " + sb.getX(), sb.getX(), sb.getY(), 1000,
					sprites.get("phaser"));
		} else if (choice == 3) {
			return new Pill("Pill at: " + sb.getY() + " " + sb.getX(), sb.getX(), sb.getY(), 700, randNum(5, 15),
					sprites.get("pill"));
		} else if (choice == 4) {
			return new ShieldOfAres("ShieldOfAres at: " + sb.getY() + " " + sb.getX(), sb.getX(), sb.getY(), 700,
					sprites.get("ares"));
		} else if (choice == 5) {
			return new SonicPower("SonicPower at: " + sb.getY() + " " + sb.getX(), sb.getX(), sb.getY(), 700);
		} else {
			return new Vest("Vest at: " + sb.getY() + " " + sb.getX(), sb.getX(), sb.getY(), 700, sprites.get("vest"));
		}
	}

	/**
	 * Returns true iff this Character's boundingBox intersects an object's
	 * boundingBox
	 * 
	 * @param list - List of stationary objects in the world map
	 * @param character - this Character
	 * @return
	 */
	public static boolean nonMovingCollision(List<GameObject> list, Character character) {
		for (GameObject o : list) {
			if (o.boundingBox().intersects(character.boundingBox()) && o.getZ() == character.getZ()) { // collision
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the Character intersecting with this Player's boundingBox
	 * 
	 * @param list - List of characters in the world map
	 * @param player - this Player
	 * @return
	 */
	public static Character movingCollision(List<Character> list, Player player) {
		for (Character c : list) {
			if (c.boundingBox().intersects(player.boundingBox()) && c.getZ() == player.getZ()) { // collision
				return c;
			}
		}
		return null;
	}

	/**
	 * Returns true iff Bullet intersects with any GameObject in the world <br>
	 * Takes care of points system
	 * 
	 * @param world - the world map
	 * @param bullet - the Bullet
	 * @return
	 */
	public static boolean bulletHit(World world, Bullet bullet) {
		for (Character c : world.getEntities()) {
			if (c.entireBoundingBox().intersects(bullet.boundingBox()) && c.getZ() == bullet.getZ()) { // collision
																										// with
																										// characters
				c.hurt(bullet.getDamage()); // damage character
				if (c.getHealth() <= 0) { // if character killed by bullet
					bullet.getPlayer().incrementKills(); // add kills of Player
															// who fired bullet
					if (c instanceof Zombie) {
						world.setZombiesRemaining(world.getZombiesRemaining() - 1);
					}
				}
				return true;
			}
		}

		for (GameObject o : world.getObjects()) {
			if (o.boundingBox().intersects(bullet.boundingBox()) && o.getZ() == bullet.getZ()) { // collision
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the Item that this Player's boundingBox intersects with <br>
	 * Automatically taken by Player
	 * 
	 * @param list - List of pickable items in the world map
	 * @param player - this Player
	 * @return
	 */
	public static Item takeItem(List<Item> list, Player player) {
		for (Item i : list) {
			if (i.boundingBox().intersects(player.boundingBox())) { // take this
																	// pill/keycard/phaser
				i.use(player); // use item
				return i;
			}
		}
		return null;
	}

	/**
	 * Returns Item near/next to the Player
	 * 
	 * @param list - List of stationary objects in the world map
	 * @param player - - this Player
	 * @return
	 */
	public static Item getNearestItem(List<GameObject> list, Player player) {
		int index = -1; // index of closest object (Item) in list
		double minDist = Double.MAX_VALUE; // current minimum distance

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) instanceof Item) { // only inspect Items
				double temp = distance(player, list.get(i)); // calculate
																// distance
				if (temp < minDist && temp < 24) { // must be lower than minDist
													// and <16
					index = i;
					minDist = temp;
				}
			}
		}

		// nothing near
		if (index == -1) {
			return null;
		} else { // return item
			return (Item) list.get(index);
		}
	}

	/**
	 * Returns int which will be used in setting currentItem at hand <br>
	 * Intended to return 0 to 9 (keyboard) including 0 to 9 (numpad) <br>
	 * Does not check for bounds so whatever method that uses this <br>
	 * must check for List bounds
	 * 
	 * @param key - the keyCode
	 * @return
	 */
	public static int getValue(int key) {
		int value = key - 48;
		if (key >= 96) {
			value -= 48;
		}
		return value;
	}

	/**
	 * Directions in the game
	 * 
	 * @author Ronni Perez
	 *
	 */
	public static enum Direction {
		NORTH, SOUTH, EAST, WEST
	}

}