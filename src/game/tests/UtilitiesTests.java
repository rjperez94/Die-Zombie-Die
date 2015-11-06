package game.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import game.Utilities;
import game.Utilities.Direction;
import game.character.Player;
import game.item.Pill;
import game.item.Crate;
import game.world.Wall;
import game.world.World;
import game.world.GameObject;

import org.junit.Test;

import com.sun.glass.events.KeyEvent;

/**
 * Tests suite for Utilities class
 * @author Ronni Perez "perezronn"
 *
 */
public class UtilitiesTests {

	@Test
	public void distanceTest1() {
		Player p = new Player("", 15*World.TILE_SIZE, 457*World.TILE_SIZE, 448);
		Pill pill = new Pill("", 15, 6984, 448, 12, null);
		assertEquals(104416, Utilities.distance(p, pill),2);
	}
	
	@Test
	public void distanceTest2() {
		Player p1 = new Player("", 15*World.TILE_SIZE, 457*World.TILE_SIZE, 448);
		Player p2 = new Player("", 15*World.TILE_SIZE, 457*World.TILE_SIZE, 448);
		assertEquals(0, Utilities.distance(p1, p2),2);
	}
	
	@Test
	public void numberGeneratorTest() {
		for (int i = 0; i < 100000; i++) {
			int num = Utilities.randNum(45, 200);
			assert(num >= 45 && num <= 200);
		}
	}
	
	@Test
	public void directionGeneratorTest() {
		for (int i = 0; i < 100000; i++) {
			assert(Utilities.randDir(Direction.NORTH) != Direction.NORTH);
			assert(Utilities.randDir(Direction.SOUTH) != Direction.SOUTH);
			assert(Utilities.randDir(Direction.WEST) != Direction.WEST);
			assert(Utilities.randDir(Direction.EAST) != Direction.EAST);
		}
	}
	
	@Test
	public void validNonMovingCollisionTest() {
		List<GameObject> list = new ArrayList<GameObject>();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
	 			Wall w = new Wall("", i, j, 1000, null, false);
				list.add(w);
			}
		}
		
		Player player =  new Player("", 5*World.TILE_SIZE, 5*World.TILE_SIZE, 1000);
		
		assertTrue(Utilities.nonMovingCollision(list, player));
	}
	
	@Test
	public void invalidNonMovingCollisionTest() {
		List<GameObject> list = new ArrayList<GameObject>();
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
	 			Wall w = new Wall("", i, j, 1000, null,false);
				list.add(w);
			}
		}
		
		Player player =  new Player("", 5*World.TILE_SIZE, 5*World.TILE_SIZE, 1000);
		
		assertFalse(Utilities.nonMovingCollision(list, player));
	}
	
	@Test
	public void nearestItemTest() {
		List<GameObject> list = new ArrayList<GameObject>();
	 	Wall w = new Wall("", 0, 0, 1000, null,false);
		list.add(w);
		Crate c = new Crate("", 3, 3, 1000, true, null);
		list.add(c);
		
		Pill item = new Pill("", 5, 5, 1000, 0, null);
		list.add(item);
		
		Player player =  new Player("", 5*World.TILE_SIZE, 5*World.TILE_SIZE, 1000);
		
		assertEquals(item, Utilities.getNearestItem(list, player));
	}
	
	@Test
	public void getValueTest() {
		int value = 0;
		for (int i = KeyEvent.VK_0; i <= KeyEvent.VK_9; i++) {
			assertEquals(value,Utilities.getValue(i));
			value++;
		}
		
		//check numpad
		value = 0;
		for (int i = KeyEvent.VK_NUMPAD0; i <= KeyEvent.VK_NUMPAD9; i++) {
			assertEquals(value,Utilities.getValue(i));
			value++;
		}
	}
}
