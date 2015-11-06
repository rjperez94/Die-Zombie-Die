package game.tests;

import static org.junit.Assert.*;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import game.Game;
import game.Utilities.Direction;
import game.character.Player;
import game.exception.FileFormatException;
import game.item.Crate;
import game.item.Door;
import game.item.KeyCard;
import game.item.Phaser;
import game.item.Pill;
import game.item.SupplyBox;
import game.item.type.Item;
import game.world.Bullet;
import game.world.Floor;
import game.world.GameObject;
import game.world.Wall;
import game.world.World;

import org.junit.Test;

/**
 * Tests suite for game.world package
 * @author Ronni Perez "perezronn"
 *
 */
public class GameWorldTests {

	@Test
	public void validParseFileTest() {
		World world = new World ("/boards/test3.txt");
		Scanner sc = new Scanner(new InputStreamReader(Game.class.getResourceAsStream("/boards/test3.txt")));
		char [][] array = new char [Integer.parseInt(sc.next())][Integer.parseInt(sc.next())];

		int row = 0;
		int col = 0;
		sc.nextLine();
		while (sc.hasNextLine()) {
			col = 0;
			String line = sc.nextLine();
			for (char c : line.toCharArray()) {
				array[row][col] = c;
				col++;
			}
			row++;
		}
		sc.close();

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				assertTrue(letterCode(array[i][j], world.getObject(i, j)));
			}
		}
	}

	//helper method
	private boolean letterCode(char c, GameObject object) {
		if (c == 'W' && object instanceof Wall) {
			return true;
		} else if (c == 'P'&& object instanceof Pill) {
			return true;
		} else if (c == 'C'&& object instanceof Crate) {
			return true;
		} else if (c == 'K'&& object instanceof KeyCard) {
			return true;
		} else if (c == 'S'&& object instanceof SupplyBox) {
			return true;
		} else if (c == 'D'&& object instanceof Door) {
			return true;
		} else if (c == '_'&& object instanceof Floor) {
			return true;
		} else if (c == 'H'&& object instanceof Phaser) {
			return true;
		} if (object == null) {
			return true;
		}
		return false;
	}

	@Test
	public void invalidParseFileTest1() {
		try {
			new World ("/boards/invalidRow.txt");
		} catch (FileFormatException e) {
			assertEquals("Illegal File Format . Lacking Number of coordinates needed", e.getMessage());
		}
	}

	@Test
	public void invalidParseFileTest2() {
		try {
			new World ("/boards/noCoordinate.txt");
		} catch (FileFormatException e) {
			assertEquals("Illegal File Format . Intended coordinate NaN", e.getMessage());
		}
	}

	@Test
	public void invalidParseFileTest3() {
		try {
			new World ( "/boards/oneCoordinate.txt");
		} catch (FileFormatException e) {
			assertEquals("Illegal File Format . Lacking Number of coordinates needed", e.getMessage());
		}
	}

	@Test	(expected=NullPointerException.class)
	public void invalidParseFileTest4() {
		new World ( "/boards/notExists.txt");
	}

	@Test
	public void takeItemTest() {
		World world = new World ( "/boards/test3.txt");
		//take pill
		world.takeItem((Item) world.getObject(3, 6), null);
		assertTrue (world.getObject(3, 6) instanceof Floor);
	}

	@Test
	public void useTest() {
		World world = new World ( "/boards/test3.txt");
		Player player = new Player("",5*World.TILE_SIZE,5*World.TILE_SIZE,10);
		//take pill
		world.use(player, (Item) world.getObject(3, 6));
		assertTrue (player.getHealth() > 100);
	}

	@Test
	public void validGeneratePhaserTest() throws InterruptedException {
		World world = new World ( "/boards/test3.txt");
		List<Item> list = world.getPickable();
		int phasersBefore = 0;
		for (Item i : list) {
			if (i instanceof Phaser) {
				phasersBefore++;
			}
		}
		Thread.sleep(1500);
		world.generate("phaser", 1000);

		int phasersAfter = 0;
		for (Item i : list) {
			if (i instanceof Phaser) {
				phasersAfter++;
			}
		}

		assertTrue (phasersAfter > phasersBefore);
	}

	@Test
	public void invalidGeneratePhaserTest() {
		World world = new World ( "/boards/test3.txt");
		List<Item> list = world.getPickable();
		int phasersBefore = 0;
		for (Item i : list) {
			if (i instanceof Phaser) {
				phasersBefore++;
			}
		}
		world.generate("phaser", 1000);

		int phasersAfter = 0;
		for (Item i : list) {
			if (i instanceof Phaser) {
				phasersAfter++;
			}
		}

		assertEquals (phasersBefore, phasersAfter);
	}

	@Test
	public void validGenerateKeyTest() throws InterruptedException {
		World world = new World ( "/boards/test3.txt");
		List<Item> list = world.getPickable();
		int keysBefore = 0;
		for (Item i : list) {
			if (i instanceof KeyCard) {
				keysBefore++;
			}
		}
		Thread.sleep(1500);
		world.generate("keycard", 1000);

		int keysAfter = 0;
		for (Item i : list) {
			if (i instanceof KeyCard) {
				keysAfter++;
			}
		}

		assertTrue (keysAfter > keysBefore);
	}

	@Test
	public void invalidGenerateKeyTest() {
		World world = new World ( "/boards/test3.txt");
		List<Item> list = world.getPickable();
		int keysBefore = 0;
		for (Item i : list) {
			if (i instanceof KeyCard) {
				keysBefore++;
			}
		}
		world.generate("phaser", 1000);

		int keysAfter = 0;
		for (Item i : list) {
			if (i instanceof KeyCard) {
				keysAfter++;
			}
		}

		assertEquals (keysBefore, keysAfter);
	}

	@Test
	public void addCharacterTest() {
		World world = new World ( "/boards/test3.txt");
		int sizeBefore = world.getEntities().size();
		world.addCharacter(new Player());
		world.addCharacter(new Player());
		assertEquals(sizeBefore+2, world.getEntities().size());
	}

	@Test
	public void removeCharacterTest() {
		World world = new World ( "/boards/test3.txt");
		int sizeBefore = world.getEntities().size();
		world.getEntities().remove(0);
		world.getEntities().remove(0);
		assertEquals(sizeBefore-2, world.getEntities().size());
	}

	@Test
	public void stationaryObjectsTest() {
		World world = new World ( "/boards/test3.txt");
		assertEquals(72, world.getObjects().size());
	}

	@Test
	public void pickableItemsTest() {
		World world = new World ( "/boards/test3.txt");
		assertEquals(5, world.getPickable().size());
	}

	@Test
	public void testBulletMove() {
		World world = new World ( "/boards/test3.txt");
		Bullet b = new Bullet(new Player("Player", 5, 5, 5), 5, 85, 65, 10, Direction.NORTH, null);

		b.update(world);
		b.update(world);
		b.update(world);

		assertEquals(5, b.getX(),2);
		assertEquals(79, b.getY(),2);
		assertEquals(65, b.getZ(),2);
	}
}
