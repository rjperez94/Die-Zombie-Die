package game.tests;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import game.Game;
import game.Utilities.Direction;
import game.character.Zombie;
import game.character.strategy.Roaming;
import game.state.GameStateManager;
import game.state.PlayState;
import game.world.World;

import org.junit.Test;

/**
 * Test suite for Zombie class
 * @author Ronni Perez "perezronn"
 *
 */
public class ZombieTests {

	@Test
	public void testDamage() {
		Zombie z = new Zombie("",10,10,10);
		assertTrue (z.damage() == 5);
	}
	
	@Test
	public void testHealth() {
		Zombie z = new Zombie("",10,10,10);
		assertTrue (z.getHealth() == 20);
	}
	
	@Test
	public void testMoveToSouth() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		PlayState state = new PlayState(new GameStateManager());
		state.init();
		Field f = state.getClass().getDeclaredField("world");
		f.setAccessible(true);
		World world = (World) f.get(state);
		
		Zombie z = new Zombie("",10,10,10);
		z.setX(Game.WIDTH / 2 - 8);
		z.setY(Game.HEIGHT / 2 - 14);
		
		Roaming strategy = new Roaming(z);
		for (int i = 0; i < 5; i++) {
			float xBefore = z.getX();
			float yBefore = z.getY();
			strategy.move(world);
			assertTrue(z.getX() == xBefore);
			assertTrue(z.getY() == yBefore+0.8f);
			assertTrue(z.isFacing() == Direction.SOUTH);
		}
	}
	
	@Test
	public void testMoveToNorth() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		PlayState state = new PlayState(new GameStateManager());
		state.init();
		Field f = state.getClass().getDeclaredField("world");
		f.setAccessible(true);
		World world = (World) f.get(state);
		
		Zombie z = new Zombie("",10,10,10);
		z.setX(Game.WIDTH / 2 - 8);
		z.setY(Game.HEIGHT / 2 - 14);
		z.setFacing(Direction.NORTH);
		
		Roaming strategy = new Roaming(z);
		for (int i = 0; i < 5; i++) {
			float xBefore = z.getX();
			float yBefore = z.getY();
			strategy.move(world);
			assertTrue(z.getX() == xBefore);
			assertTrue(z.getY() == yBefore-0.8f);
			assertTrue(z.isFacing() == Direction.NORTH);
		}
	}
	
	@Test
	public void testMoveToEast() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		PlayState state = new PlayState(new GameStateManager());
		state.init();
		Field f = state.getClass().getDeclaredField("world");
		f.setAccessible(true);
		World world = (World) f.get(state);
		
		Zombie z = new Zombie("",10,10,10);
		z.setX(Game.WIDTH / 2 - 8);
		z.setY(Game.HEIGHT / 2 - 14);
		z.setFacing(Direction.EAST);
		
		Roaming strategy = new Roaming(z);
		for (int i = 0; i < 5; i++) {
			float xBefore = z.getX();
			float yBefore = z.getY();
			strategy.move(world);
			assertTrue(z.getX() == xBefore+0.8f);
			assertTrue(z.getY() == yBefore);
			assertTrue(z.isFacing() == Direction.EAST);
		}
	}
	
	@Test
	public void testMoveToWest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		PlayState state = new PlayState(new GameStateManager());
		state.init();
		Field f = state.getClass().getDeclaredField("world");
		f.setAccessible(true);
		World world = (World) f.get(state);
		
		Zombie z = new Zombie("",10,10,10);
		z.setX(Game.WIDTH / 2 - 8);
		z.setY(Game.HEIGHT / 2 - 14);
		z.setFacing(Direction.WEST);
		
		Roaming strategy = new Roaming(z);
		for (int i = 0; i < 5; i++) {
			float xBefore = z.getX();
			float yBefore = z.getY();
			strategy.move(world);
			assertTrue(z.getX() == xBefore-0.8f);
			assertTrue(z.getY() == yBefore);
			assertTrue(z.isFacing() == Direction.WEST);
		}
	}
}
