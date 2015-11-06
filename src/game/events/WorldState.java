package game.events;

import game.character.Player;
import game.character.Zombie;
import game.item.type.Item;
import game.world.Bullet;
import game.world.World;

import java.io.Serializable;
import java.util.ArrayList;

public class WorldState implements Event, Serializable{
	private static final long serialVersionUID = -832131906371676206L;
	//	private ArrayList<Player> players;
//	private ArrayList<Zombie> zombies;
//	private ArrayList<Bullet> bullets;
//	private ArrayList<Item> items;
	private final World world;

	public WorldState(World world) {
		//this.players = players;
		this.world = world;
		//this.zombies = zombies;
		//this.bullets = bullets;
		//this.items = items;
	}
//
//	public ArrayList<Player> getPlayers() {
//		return this.players;
//	}
//
//	public ArrayList<Zombie> getZombies() {
//		return this.zombies;
//	}
//
//	public ArrayList<Bullet> getBullets() {
//		return this.bullets
//	}
//
//	public ArrayList<Item> getItems() {
//		return this.items;
//	}

	public World getWorld() {
		return world;
	}

}
