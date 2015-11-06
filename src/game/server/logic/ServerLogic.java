package game.server.logic;

import java.util.ArrayList;
import java.util.List;

import game.Utilities.Direction;
import game.character.Character;
import game.character.Player;
import game.character.Zombie;
import game.events.*;
import game.item.SonicPower;
import game.item.type.Item;
import game.network.server.Request;
import game.network.server.Server;
import game.world.Bullet;
import game.world.World;

/**
 *
 * @author Ronni Perez "perezronn"
 * @author Kieran Lewis
 *
 */
public class ServerLogic {
	private World world;
	private ArrayList<Player> players;
	// private ArrayList<Zombie> zombies;
	// private ArrayList<Bullet> bullets;
	// private ArrayList<Item> items;
	private Server server;

	public ServerLogic(World world) {
		this.world = world;
		this.players = new ArrayList<Player>();
	}

	public void init(WorldInitialise wi) {
		if (world == null) {
			world = new World("/boards/mapFile.txt");

		}
	}


	public void updateWorld() {
		// update each character
		for (int i = 0; i < world.getEntities().size(); i++) {
			Character c = world.getEntities().get(i);
			if (!(c instanceof Player)) {
				c.update(world, c.isFacing());
				if (c.getHealth() <= 0) {
					world.removeCharacter(c);
				}
			}
		}

		// update every bullet in world map
		for (int i = 0; i < world.getBullets().size(); i++) {
			world.getBullets().get(i).update(world);
		}

		// generate Phasers in world map every 30 seconds
		world.generate("phaser", 30000);
		// generate KeyCards in world map every 1 minute
		world.generate("keycard", 60000);

		WorldState state = new WorldState(world);
		this.createRequest(state);
	}

	public void createRequest(Event event) {
		Request request = new Request("server", event);
		server.getUpdateQueue().offer(request);
	}

	public void createNetwork() {
		server = new Server();
	}

	public void createNetwork(int port, int maxConnections) {
		server = new Server(port, maxConnections);
	}

	public void addPlayer(Player p) {
		this.players.add(p);
		world.addCharacter(p);
	}

	public Respawn respawnPlayer(final Respawn r) {
		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				r.getPlayer().setHealth(100);
			}
		}, 5000);

		return r;
	}

	public MovePlayer move(MovePlayer mp) {
		for (Player player : players) {
			if (player.equals(mp.getPlayer())) {
				player.update(world, mp.getDir());
			}
		}

		return mp;
	}

	public void use(UseItem ui) {
		ui.getItem().use(ui.getPlayer());
	}

	public Shoot shoot(Shoot s) {
		if (s.getPlayer().getBullets() > 0) {
			world.addBullet(new Bullet(s.getPlayer(), s.getPlayer().getX()
					+ World.TILE_SIZE / 3,
					(float) (s.getPlayer().getY() + 1.75 * s.getPlayer()
							.getFeetHeight()) + World.TILE_SIZE / 2, s
							.getPlayer().getZ(), 15, s.getPlayer().isFacing(),
					null));
			return s;
		}
		return null;
	}

	public void applyItemAtHand(ApplyItem ai) {
		Item current = ai.getPlayer().getItemAtHand();
		if (current instanceof SonicPower) {
			SonicPower sp = (SonicPower) ai.getPlayer().getItemAtHand();
			// use sonic power
			sp.use(world, ai.getPlayer());
			ai.getPlayer().removeCurrent();
		} else if (current != null) {
			current.use(ai.getPlayer());
			ai.getPlayer().removeCurrent();
		}
	}

	public Collide collide(Collide c) {
		if (c.getCharacter() instanceof Zombie) {
			c.getPlayer().hurt(Player.ZOMBIE_DAMAGE);
		} else {
			c.getPlayer().hurt(Player.PLAYER_DAMAGE);
		}
		return c;
	}


	public World getWorld() {
		return world;
	}


	public void setWorld(World world) {
		this.world = world;
	}


	public ArrayList<Player> getPlayers() {
		return players;
	}


	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}


	public Server getServer() {
		return server;
	}


	public void setServer(Server server) {
		this.server = server;
	}


}
