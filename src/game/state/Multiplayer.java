package game.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import game.Utilities;
import game.Utilities.Direction;
import game.character.Character;
import game.character.Player;
import game.events.AddPlayer;
import game.events.AllPlayers;
import game.events.ApplyItem;
import game.events.Collide;
import game.events.Event;
import game.events.MovePlayer;
import game.events.Respawn;
import game.events.Shoot;
import game.events.UseItem;
import game.events.WorldInitialise;
import game.exception.GameException;
import game.exception.GraphicsException;
import game.graphics.GraphicsLoader;
import game.graphics.Sprite;
import game.graphics.StaticSprite;
import game.item.SonicPower;
import game.item.type.Item;
import game.network.client.ClientSender;
import game.world.Floor;
import game.world.Wall;
import game.world.World;

/**
 *
 * @author Ronni Perez "perezronn"
 *
 */
public class Multiplayer extends GameState {
	private ClientSender cs;

	// this client'splayer
	private Player player;
	private List<Player> allPlayers;
	// use time -- for delay
	private long useTimer;

	// world map
	private World world;

	// background
	private Sprite starfield;

	private boolean respawned;

	public Multiplayer(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		allPlayers = new ArrayList<>();
		useTimer = 0;

		try {
			starfield = new StaticSprite(GraphicsLoader.loadImage("/backgrounds/starfield.png"));
		} catch (GraphicsException e) {
			e.printStackTrace();
		}

		cs.send(new WorldInitialise());
		WorldInitialise wi = null;
		while (wi == null) {
			wi = (WorldInitialise) cs.poll();
		}
		world = new World(wi);
		initPlayer();
	}

	/**
	 * Puts this player in a randomly selected row,col position
	 */
	private void initPlayer() {
		// make sure player does start in a Floor position
		int row = Utilities.randNum(0, World.HEIGHT - 2);
		int col = Utilities.randNum(0, World.WIDTH - 1);
		while (!(world.getObject(row, col) instanceof Floor) || !(world.getObject(row + 1, col) instanceof Floor)) {
			row = Utilities.randNum(0, World.HEIGHT - 2);
			col = Utilities.randNum(0, World.WIDTH - 1);
		}
		// put player at row col
		player = new Player("Main player", col * World.TILE_SIZE, row * World.TILE_SIZE, 1000);
		cs.send(new AddPlayer(player));
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		List<Event> evt = cs.getCurrentEvents();
		for (int i = 0; i < evt.size(); i++) {
			if (evt.get(i) instanceof MovePlayer) {
				MovePlayer mp = (MovePlayer) evt.get(i);
				movePlayer(mp);
			} else if (evt.get(i) instanceof Collide) {
				Collide c = (Collide) evt.get(i);
				collide(c);
			} else if (evt.get(i) instanceof AllPlayers) {
				AllPlayers ap = (AllPlayers) evt.get(i);
				allPlayers.clear();
				for (Player player : ap.getPlayers()) {
					if (player.UID != this.player.UID) {
						allPlayers.add(player);
					}
				}
			}
		}

		// update background
		starfield.setX(starfield.getX() - 0.4f);
		if (starfield.getX() <= -starfield.getWidth()) {
			starfield.setX(0);
		}

		Item item = player.takeItem(world);
		if (item != null) {
			cs.send(new UseItem(player, item));
		}
		Character c = player.collide(world, false);
		if (c != null) {
			cs.send(new Collide(player, c));
		}

		if (player.getHealth() <= 0) {
			if (!respawned) {
				cs.send(new Respawn(player));
				respawned = true;
			}
		} else {
			respawned = false;
		}
	}

	private void collide(Collide c) {
		// TODO Auto-generated method stub
		List<Player> chars = world.getPlayers();
		for (int i = 0; i < chars.size(); i++) {
			if (c.getPlayer().UID == chars.get(i).UID) {
				chars.set(i, c.getPlayer());
			}
		}
	}

	private void movePlayer(MovePlayer mp) {
		// TODO Auto-generated method stub

		List<Player> chars = world.getPlayers();
		for (int i = 0; i < chars.size(); i++) {
			if (mp.getPlayer().UID == chars.get(i).UID) {
				chars.set(i, mp.getPlayer());
			}
		}
	}

	@Override
	public void render(Graphics2D g2) {
		// TODO Auto-generated method stub
		// draw background
		starfield.render(g2, starfield.getX(), starfield.getY());
		starfield.render(g2, starfield.getX() + starfield.getWidth(), starfield.getY());

		// draw each object in world
		for (int row = 0; row < World.HEIGHT; row++) {
			for (int col = 0; col < World.WIDTH; col++) {
				if (world.getObject(row, col) != null) {
					world.getObject(row, col).render(g2, player.getXOffset(), player.getYOffset());
				}
			}
		}
		
		// render sonic radius as necessary
		if (player.getItemAtHand() instanceof SonicPower) {
			SonicPower sp = (SonicPower) player.getItemAtHand();
			sp.render(g2, player.getXOffset(), player.getYOffset());
		}

		// render every bullet
		for (int i = 0; i < world.getBullets().size(); i++) {
			world.getBullets().get(i).render(g2, player.getXOffset(), player.getYOffset());
		}

		// Draw 3d wall effect and game characters
		for (int row = 0; row < World.HEIGHT; row++) {
			for (int col = 0; col < World.WIDTH; col++) {
				if (world.getObject(row, col) instanceof Wall) {
					Wall w = (Wall) world.getObject(row, col);
					if (w.isTall()) {
						world.getObject(row, col).render(g2, player.getXOffset(), player.getYOffset());
					}
				}
			}

			// Draw Characters
			for (int i = 0; i < world.getEntities().size(); i++) {
				Character c = world.getEntities().get(i);
				if (c.getY() > (row - 1) * World.TILE_SIZE) {
					if (!player.equals(c)) {
						c.render(g2, player.getXOffset(), player.getYOffset());
					}
				}
			}

			if (player.getHealth() > 0 && player.getY() > (row - 1) * World.TILE_SIZE) {
				// draw this player
				player.render(g2);
			}
		}
		
		for (Player p : allPlayers) {
			p.render(g2,player.getXOffset(), player.getYOffset());
		}

		g2.setColor(Color.WHITE);
		Font f = g2.getFont();
		g2.setFont(new Font(f.getFontName(), f.getStyle(), 8));

		// TODO remove this
		g2.drawString(player.getX() + ", " + player.getY(), 10, 15);

		// List Players Statistics
		g2.drawString("KILLS: " + player.getKills(), 10, 25);
		g2.drawString("BULLETS: " + player.getBullets(), 10, 35);
		g2.setColor(Color.RED);
		if (player.getHealth() > 100) {
			g2.fillRect(10, 40, 100, 5);
		} else {
			g2.fillRect(10, 40, (int) player.getHealth(), 5);
		}
		g2.setColor(g2.getColor().darker());
		g2.drawRect(10, 40, 100, 5);

		g2.setColor(new Color(100, 149, 237)); // navy blue
		g2.fillRect(110, 40, (int) (player.getHealth() - 100), 5);
		g2.setColor(g2.getColor().darker());
		g2.drawRect(110, 40, (int) (player.getHealth() - 100), 5);

		g2.setColor(Color.WHITE);
		g2.drawString("Zombies Remaining: ", 10, 65);
		g2.drawString(Integer.toString(world.getZombiesRemaining()), 10, 75);

	}

	@Override
	public void keyPressed(int key) {
		// TODO Auto-generated method stub
		if (player.getHealth() <= 0) {
			return;
		}

		if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
			player.setFacing(Direction.NORTH);
			player.setMoving(true);
			if (player.moveTo(world, Direction.NORTH)) {
				cs.send(new MovePlayer(player, Direction.NORTH));
			}
		} else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
			player.setFacing(Direction.SOUTH);
			player.setMoving(true);
			if (player.moveTo(world, Direction.SOUTH)) {
				cs.send(new MovePlayer(player, Direction.SOUTH));
			}

		} else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
			player.setFacing(Direction.WEST);
			player.setMoving(true);
			if (player.moveTo(world, Direction.WEST)) {
				cs.send(new MovePlayer(player, Direction.WEST));
			}

		} else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
			player.setFacing(Direction.EAST);
			player.setMoving(true);
			if (player.moveTo(world, Direction.EAST)) {
				cs.send(new MovePlayer(player, Direction.EAST));
			}

		}
	}

	@Override
	public void keyReleased(int key) {
		player.setMoving(false);
		// TODO Auto-generated method stub
		if (player.getHealth() <= 0) {
			return;
		}

		int value = Utilities.getValue(key);
		// set item at hand
		if (value >= 0 && value <= 9) {
			player.setCurrentItem(value);
			// use nearest item
		} else if (key == KeyEvent.VK_E) {
			if (System.currentTimeMillis() - useTimer > 1250) {
				Item item = Utilities.getNearestItem(world.getObjects(), player);
				if (item != null) {
					cs.send(new UseItem(player, item));
					useTimer = System.currentTimeMillis();
				}
			}

			// fire weapon
		} else if (key == KeyEvent.VK_SPACE) {
			cs.send(new Shoot(player));

			// use item at hand
		} else if (key == KeyEvent.SHIFT_DOWN_MASK) {
			cs.send(new ApplyItem(player));
		} else if (key == KeyEvent.VK_I) {
			JOptionPane.showMessageDialog(null, player.showInventory(), "Items in your inventory",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void setHost(final String host, int port) {
		try {
			cs = new ClientSender(host, port);
		} catch (Exception e) {
			try {
				gsm.setCurrentState(GameStateManager.MENU_STATE);
			} catch (GameException e1) {
				e1.printStackTrace();
				System.exit(-1);
			}
		}
	}

}
