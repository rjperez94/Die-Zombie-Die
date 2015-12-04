package game.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

import game.Game;
import game.Utilities;
import game.Utilities.Direction;
import game.character.Character;
import game.character.Player;
import game.data_storage.XMLReader;
import game.data_storage.XMLWriter;
import game.exception.GameException;
import game.exception.GraphicsException;
import game.graphics.GraphicsLoader;
import game.graphics.Sprite;
import game.graphics.StaticSprite;
import game.item.SonicPower;
import game.item.type.Item;
import game.world.Bullet;
import game.world.Floor;
import game.world.MiniMap;
import game.world.Wall;
import game.world.World;

/**
 * <code>PlayState</code> is a <code>GameState</code> that runs all the gameplay
 * logic by initialing the world, player, items and other entities. It is also
 * in charge of updating and rendering all these objects to the screen when
 * required.
 *
 * @author Ronni Perez "perezronn"
 * @author Brendan Goodenough
 * @author Nathan Cooper 300305932
 * @author Kieran Lewis
 */
public class PlayState extends GameState {
	// this client'splayer
	private Player player;
	// player's direction of travel updated upon key press/release
	private Direction dir;

	// world map
	private World world;

	// background
	private Sprite starfield;

	// use item
	private Item nearestItem;
	// use time -- for delay
	private long useTimer;

	// Pause Menu
	private String[] pauseOptions = { "Resume", "Save", "Load", "Menu" };
	private int selectedPauseOption;
	private boolean paused;
	private boolean respawned;
	
	private MiniMap minimap;

	/**
	 * Constructor
	 *
	 * @param gsm - the overall interaction manager for this level
	 */
	public PlayState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		paused = false;
		
		useTimer = 0;

		try {
			starfield = new StaticSprite(GraphicsLoader.loadImage("/backgrounds/starfield.png"));
		} catch (GraphicsException e) {
			e.printStackTrace();
		}
		world = new World("/boards/mapFile.txt");

		initPlayer();
		minimap = new MiniMap(world, player);
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
	}

	@Override
	public void update() {
		if (!paused) {
			// update background
			starfield.setX(starfield.getX() - 0.4f);
			if (starfield.getX() <= -starfield.getWidth()) {
				starfield.setX(0);
			}

			// update each character
			for (int i = 0; i < world.getEntities().size(); i++) {
				Character c = world.getEntities().get(i);
				c.update(world, dir);
				if (c.getHealth() <= 0) {
					world.removeCharacter(c);
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

			if (player.getHealth() > 0) {
				// update player
				player.update(world, dir);
				if (nearestItem != null) {
					//System.out.println("Item: " +  nearestItem.getName());
					world.use(player, nearestItem);
					nearestItem = null;
				}
				respawned = false;
			} else { // respawn after 5 seconds
				if (!respawned) {
					new java.util.Timer().schedule(new java.util.TimerTask() {
						@Override
						public void run() {
							player.setHealth(100);
						}
					}, 5000);
					respawned = true;
				}
			}
		}
	}

	@Override
	public void render(Graphics2D g2) {
		// draw background
		starfield.render(g2, starfield.getX(), starfield.getY());
		starfield.render(g2, starfield.getX() + starfield.getWidth(), starfield.getY());

		// draw each object in world
		for (int row = 0; row < World.HEIGHT; row++) {
			for (int col = 0; col < World.WIDTH; col++) {
				if (world.getObject(row, col) != null){
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

		//Draw 3d wall effect and game characters
		for (int row = 0; row < World.HEIGHT; row++) {
			for (int col = 0; col < World.WIDTH; col++) {
				if (world.getObject(row, col) instanceof Wall) {
					Wall w = (Wall) world.getObject(row, col);
					if(w.isTall()){
						world.getObject(row, col).render(g2, player.getXOffset(), player.getYOffset());
					}
				}
			}

			//Draw Characters
			for(int i =0; i< world.getEntities().size(); i++){
				Character c = world.getEntities().get(i);
				if(c.getY()>(row-1)*World.TILE_SIZE){
					c.render(g2, player.getXOffset(), player.getYOffset());
				}
			}

			if (player.getHealth() > 0 && player.getY()>(row-1)*World.TILE_SIZE) {
				// draw this player
				player.render(g2);
			}
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
		if(player.getHealth()>100){
			g2.fillRect(10, 40, 100, 5);
		}
		else{
			g2.fillRect(10, 40, (int) player.getHealth(), 5);
		}
		g2.setColor(g2.getColor().darker());
		g2.drawRect(10, 40, 100, 5);

		g2.setColor(new Color(100,149,237));	//navy blue
		g2.fillRect(110, 40, (int) (player.getHealth()-100), 5);
		g2.setColor(g2.getColor().darker());
		g2.drawRect(110, 40, (int) (player.getHealth()-100), 5);

		g2.setColor(Color.WHITE);
		g2.drawString("Zombies Remaining: ", 10, 65);
		g2.drawString(Integer.toString(world.getZombiesRemaining()), 10, 75);
		
		minimap.render(g2);


		if (paused) {
			g2.setColor(new Color(0, 0, 0, 210));
			g2.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

			g2.setColor(Color.WHITE);
			g2.setFont(new Font(g2.getFont().getFontName(), Font.TRUETYPE_FONT, 12));
			g2.drawString("Die Zombie Die", 40, 40);

			for (int i = 0; i < pauseOptions.length; i++) {
				if (i == selectedPauseOption) {
					g2.setColor(new Color(240, 60, 60));
				} else {
					g2.setColor(Color.WHITE);
				}
				g2.drawString(pauseOptions[i], 40, 80 + (i * 18));
			}
		}
	}

	private void save() {
		try {
			System.out.println("Saving");
			XMLWriter writer = new XMLWriter("test_save");
			Player writtenPlayer = player;
			World writtenWorld = world;
			//Store offset as Point2d.Float in order to retain x and y float values
			//without needing to create another class just to hold this small piece of information
			//for saving and loading purposes
			Point2D.Float offSet = new Point2D.Float(player.getXOffset(), player.getYOffset());
			writer.writeGame(writtenPlayer, writtenWorld, offSet);
			writer.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void load() {
		try {
			XMLReader reader = new XMLReader("test_save.xml");
			// Load Player Information
			Player player = reader.readPlayer();
			player.loadSprites();

			// Load World Information
			World world = reader.readWorld();
			world.load();

			// Load playState offset Values
			Point2D.Float offset = reader.readOffSet();

			this.player = player;
			this.world = world;
			player.changeXOffsetTo((float) offset.getX());
			player.changeYOffsetTo((float) offset.getY());
			//this.xOffset = (float) offset.getX();
			//this.yOffset = (float) offset.getY();
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("");
		}
	}

	@Override
	public void keyPressed(int key) {
		if (!paused) {
			// set direction of travel
			if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
				dir = Direction.NORTH;
			} else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
				dir = Direction.SOUTH;
			} else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
				dir = Direction.WEST;
			} else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
				dir = Direction.EAST;
			}
		} else {
			if (key == KeyEvent.VK_UP) {
				selectedPauseOption--;

				if (selectedPauseOption < 0) {
					selectedPauseOption = pauseOptions.length - 1;
				}
			} else if (key == KeyEvent.VK_DOWN) {
				selectedPauseOption++;

				if (selectedPauseOption >= pauseOptions.length) {
					selectedPauseOption = 0;
				}
			}
		}

		if (key == KeyEvent.VK_ESCAPE) {
			paused = !paused;
		}
	}

	@Override
	public void keyReleased(int key) {
		if (!paused) {
			int value = Utilities.getValue(key);
			//set item at hand
			if (value >=0 && value <=9) {
				player.setCurrentItem(value);

			// stop travel
			} else if (dir == Direction.NORTH && (key == KeyEvent.VK_UP || key == KeyEvent.VK_W)) {
				 	dir = null;
			} else if (dir == Direction.SOUTH && (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S)) {
				 	dir = null;
			} else if (dir == Direction.WEST && (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A)) {
				 	dir = null;
			} else if (dir == Direction.EAST && (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D)) {
				 	dir = null;

			//use nearest item
			} else if (key == KeyEvent.VK_E) {
				if (System.currentTimeMillis() - useTimer > 1250) {
					// stops button spamming
					nearestItem = Utilities.getNearestItem(world.getObjects(), player);
					useTimer = System.currentTimeMillis();
				}

			//fire weapon
			} else if (key == KeyEvent.VK_SPACE) {
				dir = null;
				if (player.getBullets() > 0) {
					// fire bullet
					world.addBullet(
							new Bullet(player, player.getX() + World.TILE_SIZE / 3, (float)(player.getY()+1.75*player.getFeetHeight()) + World.TILE_SIZE / 2,
									player.getZ(), 15, player.isFacing(), null));
					player.decrementBullets();
				}

			//use item at hand
			} else if (key == KeyEvent.SHIFT_DOWN_MASK) {
				dir = null;
				Item current = player.getItemAtHand();
				if (current instanceof SonicPower) {
					SonicPower sp = (SonicPower) player.getItemAtHand();
					// use sonic power
					sp.use(world, player);
					player.removeCurrent();
				} else if (current != null) {
					current.use(player);
					player.removeCurrent();
				}
			} else if (key == KeyEvent.VK_I ){
				dir = null;
				JOptionPane.showMessageDialog(null, player.showInventory(), "Items in your inventory", JOptionPane.INFORMATION_MESSAGE);
			}
		} else if (key == KeyEvent.VK_ENTER) {
			switch (selectedPauseOption) {
			case 0:
				paused = false;
				break;
			case 1:
				save();
				break;
			case 2:
				load();
				break;
			case 3:
				try {
					gsm.setCurrentState(GameStateManager.MENU_STATE);
				} catch (GameException e) {
				}
				break;
			}
		}
	}
}
