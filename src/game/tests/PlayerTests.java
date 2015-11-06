package game.tests;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import game.Game;
import game.Utilities.Direction;
import game.character.Player;
import game.item.KeyCard;
import game.item.Pill;
import game.item.ShieldOfAres;
import game.item.Vest;
import game.item.type.Item;
import game.state.GameStateManager;
import game.state.PlayState;
import game.world.World;

import org.junit.Test;

/**
 * Test suite for the Player class
 * 
 * @author Ronni Perez "perezronn"
 *
 */
public class PlayerTests {

	@Test
	public void testGetters() {
		Player p = new Player("Ron", 1451, 4587, 1548);
		assertEquals(1451, p.getX(), 2);
		assertEquals(4587, p.getY(), 2);
		assertEquals(1548, p.getZ(), 2);
		assertEquals(100, p.getHealth(), 2);
		assertEquals(1f, p.getVelocity(), 2);
		assertEquals(Direction.SOUTH, p.isFacing());
		assertFalse(p.isMoving());
	}

	@Test
	public void testReplenish() {
		Player p = new Player("Ron", 1451, 4587, 1548);
		p.replenish(100);
		assertEquals(200, p.getHealth(), 2);
	}

	@Test
	public void validHasItem() {
		Player p = new Player("Ron", 1451, 4587, 1548);
		ShieldOfAres shield = new ShieldOfAres("", 10, 10, 10, null);
		p.addToInventory(shield);
		assertTrue(p.hasItem(shield));
	}

	@Test
	public void invalidHasItem1() {
		Player p = new Player("Ron", 1451, 4587, 1548);
		assertFalse(p.hasItem(new ShieldOfAres("", 10, 10, 10, null)));
	}

	@Test
	public void invalidHasItem2() {
		Player p = new Player("Ron", 1451, 4587, 1548);
		assertFalse(p.hasItem(null));
	}

	@Test
	public void invalidHasItem3() {
		Player p = new Player("Ron", 1451, 4587, 1548);
		for (int i = 0; i < 10; i++) {
			ShieldOfAres shield = new ShieldOfAres("Shield", 10, 10, 10, null);
			p.addToInventory(shield);
		}
		assertFalse(p.hasItem(new Pill("", 10, 10, 10, 0, null)));
	}

	@Test
	public void validHasKey() {
		Player p = new Player("Ron", 1451, 4587, 1548);
		KeyCard key = new KeyCard("KeyCard", 10, 10, 10, null);
		p.addToInventory(key);
		assertTrue(p.hasKey());
	}

	@Test
	public void invalidHasKey1() {
		Player p = new Player("Ron", 1451, 4587, 1548);
		assertFalse(p.hasKey());
	}

	@Test
	public void invalidHasKey2() {
		Player p = new Player("Ron", 1451, 4587, 1548);
		for (int i = 0; i < 9; i++) {
			ShieldOfAres shield = new ShieldOfAres("Shield", 10, 10, 10, null);
			p.addToInventory(shield);
		}
		assertFalse(p.hasKey());
	}

	@Test
	public void validAddToInventory() {
		Player p = new Player("Ron", 1451, 4587, 1548);
		for (int i = 0; i < 10; i++) {
			ShieldOfAres shield = new ShieldOfAres("Shield", 10, 10, 10, null);
			assertTrue(p.addToInventory(shield));
		}
	}

	@Test
	public void invalidAddToInventory() {
		Player p = new Player("Ron", 1451, 4587, 1548);
		for (int i = 0; i < 20; i++) {
			ShieldOfAres shield = new ShieldOfAres("Shield", 10, 10, 10, null);
			if (i > 10) {
				assertFalse(p.addToInventory(shield));
			} else {
				p.addToInventory(shield);
			}
		}
	}

	@Test
	public void testMoveToSouth() throws NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {
		PlayState state = new PlayState(new GameStateManager());
		state.init();

		Field f = state.getClass().getDeclaredField("player");
		f.setAccessible(true);
		Field f2 = state.getClass().getDeclaredField("world");
		f2.setAccessible(true);

		Player p = (Player) f.get(state);
		p.setX(Game.WIDTH / 2 - 8);
		p.setY(Game.HEIGHT / 2 - 14);

		World world = (World) f2.get(state);

		for (int i = 0; i < 3; i++) {
			float xBefore = p.getX();
			float yBefore = p.getY();
			p.update(world, Direction.SOUTH);
			assertTrue(p.getX() == xBefore);
			assertTrue(p.getY() > yBefore);
			assertTrue(p.isFacing() == Direction.SOUTH);
		}
	}

	@Test
	public void testMoveToNorth() throws NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {
		PlayState state = new PlayState(new GameStateManager());
		state.init();

		Field f = state.getClass().getDeclaredField("player");
		f.setAccessible(true);
		Field f2 = state.getClass().getDeclaredField("world");
		f2.setAccessible(true);

		Player p = (Player) f.get(state);
		p.setX(Game.WIDTH / 2 - 8);
		p.setY(Game.HEIGHT / 2 - 14);

		World world = (World) f2.get(state);

		for (int i = 0; i < 3; i++) {
			float xBefore = p.getX();
			float yBefore = p.getY();
			p.update(world, Direction.NORTH);
			assertTrue(p.getX() == xBefore);
			assertTrue(p.getY() < yBefore);
			assertTrue(p.isFacing() == Direction.NORTH);
		}
	}

	@Test
	public void testMoveToEast() throws NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {
		PlayState state = new PlayState(new GameStateManager());
		state.init();

		Field f = state.getClass().getDeclaredField("player");
		f.setAccessible(true);
		Field f2 = state.getClass().getDeclaredField("world");
		f2.setAccessible(true);

		Player p = (Player) f.get(state);
		p.setX(Game.WIDTH / 2 - 8);
		p.setY(Game.HEIGHT / 2 - 14);

		World world = (World) f2.get(state);

		for (int i = 0; i < 3; i++) {
			float xBefore = p.getX();
			float yBefore = p.getY();
			p.update(world, Direction.EAST);
			assertTrue(p.getX() > xBefore);
			assertTrue(p.getY() == yBefore);
			assertTrue(p.isFacing() == Direction.EAST);
		}
	}

	@Test
	public void testMoveToWest() throws NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {
		PlayState state = new PlayState(new GameStateManager());
		state.init();

		Field f = state.getClass().getDeclaredField("player");
		f.setAccessible(true);
		Field f2 = state.getClass().getDeclaredField("world");
		f2.setAccessible(true);

		Player p = (Player) f.get(state);
		p.setX(Game.WIDTH / 2 - 8);
		p.setY(Game.HEIGHT / 2 - 14);

		World world = (World) f2.get(state);

		for (int i = 0; i < 3; i++) {
			float xBefore = p.getX();
			float yBefore = p.getY();
			p.update(world, Direction.WEST);
			assertTrue(p.getX() < xBefore);
			assertTrue(p.getY() == yBefore);
			assertTrue(p.isFacing() == Direction.WEST);
		}
	}

	@Test
	public void testIncreaseBullets() {
		Player p = new Player("Ron", 1451, 4587, 1548);
		p.incrementBullets(14);
		assertEquals(14, p.getBullets());
	}

	@Test
	public void testDecreaseBullets() {
		Player p = new Player("Ron", 1451, 4587, 1548);
		p.incrementBullets(14);
		for (int i = 1; i <= 14; i++) {
			p.decrementBullets();
			assertEquals(14 - i, p.getBullets());
		}
	}

	@Test
	public void testKills() {
		Player p = new Player("Ron", 1451, 4587, 1548);

		for (int i = 1; i <= 14; i++) {
			p.incrementKills();
			assertEquals(i, p.getKills());
		}
	}

	@Test
	public void validCurrentItem() {
		Player p = new Player("Ron", 1451, 4587, 1548);
		List<Item> local = new ArrayList<Item>();

		ShieldOfAres shield = new ShieldOfAres("Shield", 10, 10, 10, null);
		p.addToInventory(shield);
		local.add(shield);

		Pill pill = new Pill("Pill", 10, 10, 10, 0, null);
		p.addToInventory(pill);
		local.add(pill);

		KeyCard key = new KeyCard("Key", 10, 10, 10, null);
		p.addToInventory(key);
		local.add(key);

		Vest vest = new Vest("Vest", 10, 10, 10, null);
		p.addToInventory(vest);
		local.add(vest);

		for (int i = 0; i < 4; i++) {
			p.setCurrentItem(i);
			assertEquals(local.get(i), p.getItemAtHand());
		}
	}

	@Test
	public void invalidCurrentItem() {
		Player p = new Player("Ron", 1451, 4587, 1548);
		p.setCurrentItem(4);
		assertEquals(null, p.getItemAtHand());
	}
}