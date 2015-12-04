package game.world;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.Game;
import game.Utilities;
import game.character.Character;
import game.character.Player;
import game.character.Zombie;
import game.exception.FileFormatException;
import game.exception.GameException;
import game.exception.GraphicsException;
import game.graphics.Sprite;
import game.graphics.SpriteSheet;
import game.item.Phaser;
import game.item.Pill;
import game.item.Crate;
import game.item.KeyCard;
import game.item.SupplyBox;
import game.item.Door;
import game.item.type.Item;

/**
 * This represents the game 'map'
 * 
 * @author Ronni Perez "perezronn"
 * @author Nathan Cooper 300305932
 *
 */
public class World implements Serializable {
	private static final long serialVersionUID = -4021003231765022688L;

	private GameObject[][] objects; // underlying data structure
	private List<Character> entities; // all characters in this map
	private List<GameObject> stationaryObj; // all stationary objects except
											// including Items not pickable
	private List<Item> pickableItems; // all items that can be picked by walking
										// on them
	private List<Bullet> bullets; // bullets in the map
	private List<Floor> floorLocations; // floor Locations where a zombie can
										// spawn
	private Map<String, Sprite> sprites; // Mapping of sprite names to sprites

	// Dimensions
	public static int WIDTH;
	public static int HEIGHT;
	public static final int TILE_SIZE = 16;

	// Save Details
	private String fileName; // File from which the game World is created from
	private int zombiesRemaining; // Number of Zombies still in the game

	// Zombie density
	private final int density = 30;

	// auto gen timer
	private long phaserGenerateTimer;
	private long keyGenerateTimer;

	private List<Zombie> zombies;
	private List<Player> players;

	/**
	 * Constructor
	 * 
	 * @param state - the game level
	 * @param fileName - relative target of map file to be used
	 */
	public World(String fileName) {
		this.fileName = fileName;
		init();

		parseFile(fileName); // parse text file

		generateZombies(density);
		setupDoors();

		// set time of initial generate
		phaserGenerateTimer = System.currentTimeMillis();
		keyGenerateTimer = System.currentTimeMillis();
	}

	/**
	 * Creates the base instances of the various collections
	 */
	private void init() {
		this.sprites = loadWorldSprites();
		this.entities = new ArrayList<Character>();
		this.stationaryObj = new ArrayList<GameObject>();
		this.pickableItems = new ArrayList<Item>();
		this.bullets = new ArrayList<Bullet>();
		this.floorLocations = new ArrayList<Floor>();
	}

	/**
	 * Parses a given file containing the world map and assigns an appropriate
	 * sprite to the parsed objects <br>
	 * Assumes file format is valid
	 * 
	 * @param state - the game level
	 * @param fileName - relative target of map file to be used
	 * @param sprites - HashMap of Sprite each corresponding to a GameObject
	 */
	private void parseFile(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(Game.class.getResourceAsStream(fileName)));
			// reads dimensions in: HEIGHT WIDTH e.g. 10 21
			String[] dim = br.readLine().split("\\s+");
			World.HEIGHT = Integer.parseInt(dim[0]);
			World.WIDTH = Integer.parseInt(dim[1]);
			// construct length of data structure appropriately
			this.objects = new GameObject[World.HEIGHT][World.WIDTH];

			// list of doors , for used for assigning special locked doors
			List<Door> doors = new ArrayList<Door>();
			// list of supply boxes
			List<SupplyBox> supplies = new ArrayList<SupplyBox>();

			String line;
			int row = 0, col = 0;
			// iterate through each line w/o spaces e.g. W____W____W___
			// put objects in list of stationary objects as necessary
			while ((line = br.readLine()) != null) {
				col = 0;
				for (char c : line.toCharArray()) {
					if (c == 'W') {
						if (row != 0 && !(objects[row - 1][col] instanceof Wall || objects[row - 1][col] == null
								|| objects[row - 1][col] instanceof Door)) {
							// This wall can cover characters
							objects[row][col] = new Wall("Wall at: " + row + " " + col, col, row, 1000,
									sprites.get("tallwall"), true);
						} else {
							// This wall can't cover characters
							objects[row][col] = new Wall("Wall at: " + row + " " + col, col, row, 1000,
									sprites.get("wall"), false);
						}
						stationaryObj.add((Wall) objects[row][col]);
					} else if (c == 'P') {
						objects[row][col] = new Pill("Pill at: " + row + " " + col, col, row, 700,
								Utilities.randNum(5, 15), sprites.get("pill"));
						pickableItems.add((Item) objects[row][col]);
					} else if (c == 'C') {
						objects[row][col] = new Crate("Crate at: " + row + " " + col, col, row, 1000, true,
								sprites.get("crate"));
						stationaryObj.add((Crate) objects[row][col]);
					} else if (c == 'K') {
						objects[row][col] = new KeyCard("KeyCard at: " + row + " " + col, col, row, 700,
								sprites.get("key"));
						pickableItems.add((Item) objects[row][col]);
					} else if (c == 'S') {
						objects[row][col] = new SupplyBox("SupplyBox at: " + row + " " + col, col, row, 450,
								sprites.get("supply"));
						stationaryObj.add((SupplyBox) objects[row][col]);
						supplies.add((SupplyBox) objects[row][col]);
					} else if (c == 'D') {
						objects[row][col] = new Door("Door at :" + row + " " + col, col, row, 1000,
								sprites.get("door"));
						stationaryObj.add((Door) objects[row][col]);
						doors.add((Door) objects[row][col]);
					} else if (c == '_') {
						objects[row][col] = new Floor("Floor at :" + row + " " + col, col, row, 0,
								sprites.get("floor"));
						floorLocations.add((Floor) objects[row][col]);
					} else if (c == 'H') {
						objects[row][col] = new Phaser("Phaser at :" + row + " " + col, col, row, 1000,
								sprites.get("phaser"));
						pickableItems.add((Item) objects[row][col]);
					}
					col++;
					// check for column limit
					if (col > World.WIDTH + 1) {
						throw new FileFormatException("Illegal File Format . Too many columns");
					}
				}
				row++;
			}
			// check for row limit
			if (row > World.HEIGHT) {
				throw new FileFormatException("Illegal File Format . Too many rows");
			}

			// assign 1 special locked door-pair
			// chosen door
			if (!doors.isEmpty()) {
				Door d = doors.get(Utilities.randNum(0, doors.size() - 1));
				d.setNeedKey(true);
				d.sprite = sprites.get("lockeddoor");
				lockDoorPair(d);
			}
			// generate items inside supply boxes
			putRandomItemsIn(supplies, sprites);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new FileFormatException("Illegal File Format . Lacking Number of coordinates needed");
		} catch (NumberFormatException e) {
			throw new FileFormatException("Illegal File Format . Intended coordinate NaN");
		}
	}

	/**
	 * Put Items inside every SupplyBox in this world map <br>
	 * The number of Items put inside each SupplyBox varies from 5 to 10 items
	 * 
	 * @param supplies - the List of SupplyBox
	 * @param sprites
	 */
	private void putRandomItemsIn(List<SupplyBox> supplies, Map<String, Sprite> sprites) {
		for (SupplyBox sb : supplies) {
			int additions = Utilities.randNum(5, 10);
			while (additions > 0) {
				sb.putIn(Utilities.randItem(sb, sprites));
				additions--;
			}

		}
	}

	/**
	 * Lock the pair of this Door
	 * 
	 * @param d - this Door
	 */
	private void lockDoorPair(Door d) {
		// coodrinates of chosen Door
		int row = (int) d.getY();
		int col = (int) d.getX();

		// lock Door pair
		if (objects[row + 1][col] instanceof Door) {
			// look down
			((Door) objects[row + 1][col]).setNeedKey(true);
			((Door) objects[row + 1][col]).sprite = sprites.get("lockeddoor");
		}
		if (objects[row - 1][col] instanceof Door) {
			// look up
			((Door) objects[row - 1][col]).setNeedKey(true);
			((Door) objects[row - 1][col]).sprite = sprites.get("lockeddoor");
		}
		if (objects[row][col + 1] instanceof Door) {
			// look right
			((Door) objects[row][col + 1]).setNeedKey(true);
			((Door) objects[row][col + 1]).sprite = sprites.get("lockeddoor");
		}
		if (objects[row][col - 1] instanceof Door) {
			// look left
			((Door) objects[row][col - 1]).setNeedKey(true);
			((Door) objects[row][col - 1]).sprite = sprites.get("lockeddoor");
		}
	}

	/**
	 * Returns a Map<String,Sprite> containing graphics set for the world
	 * 
	 * @return
	 */
	private Map<String, Sprite> loadWorldSprites() {
		Map<String, Sprite> sprites = new HashMap<>();
		try {
			SpriteSheet spritesheet = new SpriteSheet("/spritesheets/textures.png", 8, 8);
			sprites.put("floor", spritesheet.getSprite(0, 0));
			sprites.put("wall", spritesheet.getSprite(0, 1));

			Sprite tallWall = spritesheet.getSprite(1, 1);
			tallWall.setHeight((int) (tallWall.getHeight() * 1.5));
			sprites.put("tallwall", tallWall);

			sprites.put("pill", spritesheet.getSprite(0, 2));
			sprites.put("crate", spritesheet.getSprite(0, 3));
			sprites.put("key", spritesheet.getSprite(0, 4));
			sprites.put("supply", spritesheet.getSprite(0, 5));
			sprites.put("door", spritesheet.getSprite(0, 6));
			sprites.put("lockeddoor", spritesheet.getSprite(1, 6));
			sprites.put("phaser", spritesheet.getSprite(0, 7));
			// TODO add graphics
			sprites.put("ares", null);
			sprites.put("vest", null);
		} catch (GraphicsException e) {
			e.printStackTrace();
		}

		return sprites;
	}

	/**
	 * Setup the goto location for the doors
	 */
	private void setupDoors() {
		for (GameObject obj : stationaryObj) {
			if (obj instanceof Door) {
				Door d = (Door) obj;
				int col = (int) (d.getX());
				int row = (int) (d.getY());

				if (row > 0 && objects[row - 1][col] instanceof Door) {
					d.setSendToX(2 * World.TILE_SIZE);
				} else if (row < objects.length - 1 && objects[row + 1][col] instanceof Door) {
					d.setSendToX(2 * World.TILE_SIZE);
				} else if (col > 0 && objects[row][col - 1] instanceof Door) {
					d.setSendToY(2 * World.TILE_SIZE);
				} else if (col < objects[0].length - 1 && objects[row][col + 1] instanceof Door) {
					d.setSendToY(2 * World.TILE_SIZE);
				}
			}
		}
	}

	/**
	 * Spawns one zombie with every n floor tiles as defined by frequency Lower
	 * frequency = more Zombies Higher frequency = less Zombies
	 * 
	 * @param frequency - How often to spawn zombies
	 * @throws GameException
	 */
	private void generateZombies(int frequency) {
		if (0 <= frequency || frequency >= floorLocations.size()) {

		}

		for (int i = 0; i < floorLocations.size() && i + frequency <= floorLocations.size(); i = i + frequency) {
			if (entities.size() >= Math.ceil(floorLocations.size() / frequency)) {
				return;
			}
			int low = i;
			int high = i + frequency;
			int spawnPoint = Utilities.randNum(0, frequency - 1);
			List<Floor> spawnOptions = floorLocations.subList(low, high);
			Floor pos = spawnOptions.get(spawnPoint);
			float x = pos.getX();
			float y = pos.getY() - 1;
			entities.add(
					new Zombie("Zombie started at: " + y + " " + x, x * World.TILE_SIZE, y * World.TILE_SIZE, 1000));
			zombiesRemaining++;
		}
	}

	/**
	 * Removes Item from List of pickableItem and replaces it's location as a
	 * Floor <br>
	 * Assume that Item is not null
	 * 
	 * @param i - Item to replace
	 * @param sprite - Floor Sprite/graphic
	 */
	public void takeItem(Item i, String sprite) {
		int row = (int) i.yLoc; // get coordinates
		int col = (int) i.xLoc;
		// put floor in place of pill
		objects[row][col] = new Floor("Floor at :" + row + " " + col, col, row, 0, sprites.get(sprite));
		// remove pill from list of pills
		pickableItems.remove(i);
	}

	/**
	 * Updates nearestItem when player uses it
	 * 
	 * @param player - the Player
	 * @param nearestItem - the Item with shortest distance to Player
	 */
	public void use(Player player, Item nearestItem) {
		if (nearestItem instanceof Crate) {
			Crate c = (Crate) nearestItem;
			c.use(this, player, sprites.get("floor"));
		} else {
			nearestItem.use(player);
		}
	}

	/**
	 * Generates (RANDOM) 1 to 5 phasers OR 1 to 2 keys This is applicable to
	 * the whole map in a RANDOM Floor location
	 * 
	 * @param item - the Item identifier to generate ("phaser" OR "keycard")
	 * @param delay -- generation delay in milliseconds
	 */
	public void generate(String item, int delay) {
		if ((System.currentTimeMillis() - phaserGenerateTimer > delay && item.equals("phaser"))
				|| (System.currentTimeMillis() - keyGenerateTimer > delay && item.equals("keycard"))) { // generate
																										// N
																										// milliseconds
			int additions = 0;
			if (item.equals("phaser")) {
				additions = Utilities.randNum(1, 5); // number of generated
														// phasers for whole map
			} else if (item.equals("keycard")) {
				additions = Utilities.randNum(1, 2); // number of generated keys
														// for whole map
			}
			while (additions > 0) {
				int row = Utilities.randNum(0, World.HEIGHT - 1);
				int col = Utilities.randNum(0, World.WIDTH - 1);

				while (!(objects[row][col] instanceof Floor)) { // if row col is
																// not Floor
																// i.e. not free
																// space
					row = Utilities.randNum(0, World.HEIGHT - 1);
					col = Utilities.randNum(0, World.WIDTH - 1);
				}
				// put Phaser or KeyCard at row,col
				if (item.equals("phaser")) {
					objects[row][col] = new Phaser("Phaser at :" + row + " " + col, col, row, 1000,
							sprites.get("phaser"));
				} else if (item.equals("keycard")) {
					objects[row][col] = new KeyCard("KeyCard at :" + row + " " + col, col, row, 700,
							sprites.get("key"));
				}
				pickableItems.add((Item) objects[row][col]);

				additions--;
			}

			if (item.equals("phaser")) {
				// reset timer
				phaserGenerateTimer = System.currentTimeMillis();
			} else if (item.equals("keycard")) {
				// reset timer
				keyGenerateTimer = System.currentTimeMillis();
			}

		}
	}

	/**
	 * Returns stationary GameObject at row, col (i.e. NOT INCLUDING characters)
	 * 
	 * @param row - row coordinate
	 * @param col - column coordinate
	 * @return
	 */
	public GameObject getObject(int row, int col) {
		return objects[row][col];
	}

	/**
	 * Set 2D array objects at row,col as the GameObject
	 * 
	 * @param row - row coordinate
	 * @param col - column coordinate
	 * @param newObj - the new GameObject in this index
	 */
	public void setObject(int row, int col, GameObject newObj) {
		objects[row][col] = newObj;
	}

	/**
	 * Add Character to world map
	 * 
	 * @param c
	 */
	public void addCharacter(Character c) {
		entities.add(c);
	}

	/**
	 * Removes the first occurrence of the specified Character from this list of
	 * Characters, if it is present Returns true iff this list contained the
	 * specified element
	 * 
	 * @param c - the Character to remove
	 * @return
	 */
	public boolean removeCharacter(Character c) {
		return entities.remove(c);
	}

	/**
	 * @return the characters in the world map
	 */
	public List<Character> getEntities() {
		return entities;
	}

	/**
	 * Return list of stationary GameObjects in world map
	 * 
	 * @return
	 */
	public List<GameObject> getObjects() {
		return stationaryObj;
	}

	/**
	 * Returns List of pickable items in this world map
	 * 
	 * @return
	 */
	public List<Item> getPickable() {
		return pickableItems;
	}

	/**
	 * List of Bullets in world map
	 * 
	 * @return
	 */
	public List<Bullet> getBullets() {
		return bullets;
	}

	/**
	 * Adds the specified Bullet object to the world
	 * 
	 * @param bullet
	 */
	public void addBullet(Bullet bullet) {
		bullets.add(bullet);
	}

	/**
	 * Removes the specified Bullet from the world
	 * 
	 * @param bullet
	 */
	public void removeBullet(Bullet bullet) {
		bullets.remove(bullet);
	}

//	public List<Zombie> getZombies() {
//		// TODO Auto-generated method stub
//		return zombies;
//	}
//
//	public List<Player> getPlayers() {
//		// TODO Auto-generated method stub
//		return players;
//	}
//
//	public GameObject[][] getArray() {
//		return objects;
//
//	}

	// Code relevant only to Loading and Saving
	/**
	 * Don't use this constructor should only be used when loading from xml
	 */
	public World() {
		init();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setPickable(List<Item> pickableItems) {
		this.pickableItems = pickableItems;
	}

	public void setEntities(List<Character> entities) {
		this.entities = entities;
	}

	public int getZombiesRemaining() {
		return zombiesRemaining;
	}

	public void setZombiesRemaining(int zombiesRemaining) {
		this.zombiesRemaining = zombiesRemaining;
	}

	public void setBullets(List<Bullet> bullets) {
		this.bullets = bullets;
	}

	/***
	 * Load the map from file and restore it to state of saved game
	 * 
	 * @param playState
	 */
	public void load() {
		ArrayList<Item> loadedPickableItems = new ArrayList<>(pickableItems); // List
																				// Loaded
		parseFile(fileName);
		// Restore to loaded list as parseFile replaced it
		List<Item> temp = new ArrayList<>(pickableItems);
		for (Item item : temp) {
			if (!loadedPickableItems.contains(item)) {
				takeItem(item, "floor");
			}
		}
		setPickable(loadedPickableItems);
		setupDoors();
	}
}