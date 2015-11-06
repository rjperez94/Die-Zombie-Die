package game.world;

import java.awt.Color;
import java.awt.Graphics2D;

import javafx.geometry.BoundingBox;
import game.Utilities;
import game.Utilities.Direction;
import game.character.Player;
import game.graphics.Sprite;

/**
 * This represents a 'bullet' fired from a Phaser
 * @author Ronni Perez "perezronn"
 *
 */
public class Bullet extends GameObject {
	private Direction dir;	//direction of travel
	private float increment;
	private int damage;
	private Player player;

	/**
	 * Constructor
	 * @param name - bullet alias
	 * @param xStart - initial x position
	 * @param yStart - initial y position
	 * @param zStart - initial z position
	 * @param damage -  bullet's RANDOM hit damage (5 -- 15 incl)
	 * @param direction - bullet's direction of travel
	 * @param sprite
	 */
	public Bullet (Player player, float xStart, float yStart, float zStart, int damage, Direction direction, Sprite sprite) {
		super("Bullet", xStart, yStart, zStart, sprite);
		this.player = player;
		this.damage = damage;
		this.dir = direction;
		this.increment = 2;
	}

	@Override
	public void render(Graphics2D g2, float xOffset, float yOffset) {
//		if (damage < 5) {
//			g2.setColor(Color.BLUE);
//		} else if (damage < 10) {
//			g2.setColor(Color.ORANGE);
//		} else if (damage < 15) {
//			g2.setColor(Color.RED);
//		}
		g2.setColor(Color.BLUE);
		g2.fillRect((int)(xOffset + (xLoc)), (int)(yOffset + (yLoc)), World.TILE_SIZE/4, World.TILE_SIZE/4);
		g2.setColor(g2.getColor().darker());
		g2.drawRect((int)(xOffset + (xLoc)), (int)(yOffset + (yLoc)), World.TILE_SIZE/4, World.TILE_SIZE/4);
	}

	/**
	 * Updates abullet's state in a given tick
	 * @param world - the world map
	 */
	public void update (World world) {
		boolean collided = Utilities.bulletHit(world, this);	//check for collision

		if (collided) {	//collided
			world.removeBullet(this);
		} else {
			move();
		}
	}

	/**
	 * Move this Bullet towards given direction
	 * <br>Assume valid move
	 */
	private void move() {
		if (dir==Direction.NORTH) {
			yLoc -= increment;
		} else if (dir==Direction.SOUTH) {
			yLoc+=increment;
		} else if (dir==Direction.WEST) {
			xLoc-=increment;
		} else if (dir == Direction.EAST) {
			xLoc+=increment;
		}
	}

	@Override
	public BoundingBox boundingBox() {
		return new BoundingBox(xLoc, yLoc, World.TILE_SIZE/2, World.TILE_SIZE/2);
	}

	/**
	 * Return bullet damage
	 * @return
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Returns Player who fired the bullet
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}

	//Load Save Code

	/**
	 * Don't use this constructor should only be used when loading from xml
	 */
	public Bullet(){

	}

	public Direction getDir() {
		return dir;
	}

	public void setDir(Direction dir) {
		this.dir = dir;
	}

	public float getIncrement() {
		return increment;
	}

	public void setIncrement(float increment) {
		this.increment = increment;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
