package game.tests;

import static org.junit.Assert.*;
import game.Utilities.Direction;
import game.character.Player;
import game.item.Crate;
import game.item.Door;
import game.item.KeyCard;
import game.item.Phaser;
import game.item.Pill;
import game.item.ShieldOfAres;
import game.item.SonicPower;
import game.item.SupplyBox;
import game.item.Vest;
import game.state.GameStateManager;
import game.state.PlayState;
import game.world.World;

import java.lang.reflect.Field;

import org.junit.Test;

/**
 * Test suite for all Items and subtypes of it including Weapon and Equipment
 * @author Ronni Perez "perezronn"
 *
 */
public class ItemsTests {

	@Test
	public void testCrateMoveNorth() {
		World world = new World("/boards/test4.txt");

		Player player = new Player("",10*World.TILE_SIZE,10*World.TILE_SIZE,10);
		player.setFacing(Direction.NORTH);
		Crate item = new Crate ("", 4,5,1000,true, null);

		for (int i = 0; i < 3; i++) {
			float xBefore = item.getX();;
			item.use(world, player, null);
			assertTrue(item.getX() == xBefore);
		}
	}

	@Test
	public void testCrateMoveSouth() {
		World world = new World("/boards/test4.txt");

		Player player = new Player("",10*World.TILE_SIZE,10*World.TILE_SIZE,1000);
		player.setFacing(Direction.SOUTH);
		Crate item = new Crate ("", 4,5,1000,true, null);

		for (int i = 0; i < 3; i++) {
			float xBefore = item.getX();
			item.use(world, player, null);
			assertTrue(item.getX() == xBefore);
		}
	}

	@Test
	public void testCrateMoveWest() {
		World world = new World("/boards/test4.txt");

		Player player = new Player("",10*World.TILE_SIZE,10*World.TILE_SIZE,1000);
		player.setFacing(Direction.WEST);
		Crate item = new Crate ("", 4,5,1000,true, null);

		for (int i = 0; i < 3; i++) {
			float yBefore = item.getY();
			item.use(world, player, null);
			assertTrue(item.getY() == yBefore);
		}
	}

	@Test
	public void testCrateMoveEast() {
		World world = new World("/boards/test4.txt");

		Player player = new Player("",10*World.TILE_SIZE,10*World.TILE_SIZE,1000);
		player.setFacing(Direction.EAST);
		Crate item = new Crate ("", 2,2,1000,true, null);

		for (int i = 0; i < 3; i++) {
			float yBefore = item.getY();
			item.use(world, player, null);
			assertTrue(item.getY() == yBefore);
		}
	}

	@Test
	public void testDoorToEast() {
		Door d = new Door("Door", 10, 10, 1000, null);
		d.setSendToX(2*World.TILE_SIZE);

		Player player = new Player("",9*World.TILE_SIZE,10*World.TILE_SIZE,10);
		player.setFacing(Direction.EAST);

		d.use(player);
		assertEquals((d.getX()+1)*World.TILE_SIZE, player.getX(), 2);
		assertEquals((d.getY())*World.TILE_SIZE, player.getY(), 2);
	}

	@Test
	public void testDoorToWest() {
		Door d = new Door("Door", 10, 10, 1000, null);
		d.setSendToX(2*World.TILE_SIZE);

		Player player = new Player("",11*World.TILE_SIZE,10*World.TILE_SIZE,10);
		player.setFacing(Direction.WEST);

		d.use(player);
		assertEquals((d.getX()-1)*World.TILE_SIZE, player.getX(), 2);
		assertEquals((d.getY())*World.TILE_SIZE, player.getY(), 2);
	}

	@Test
	public void testDoorToNorth() {
		Door d = new Door("Door", 10, 10, 1000, null);
		d.setSendToY(2*World.TILE_SIZE);

		Player player = new Player("",10*World.TILE_SIZE,11*World.TILE_SIZE,10);
		player.setFacing(Direction.NORTH);

		d.use(player);
		assertEquals((d.getX())*World.TILE_SIZE, player.getX(), 2);
		assertEquals((d.getY()-1)*World.TILE_SIZE, player.getY(), 2);
	}

	@Test
	public void testDoorToSouth() {
		Door d = new Door("Door", 10, 10, 1000, null);
		d.setSendToY(2*World.TILE_SIZE);

		Player player = new Player("",10*World.TILE_SIZE,9*World.TILE_SIZE,10);
		player.setFacing(Direction.SOUTH);

		d.use(player);
		assertEquals((d.getX())*World.TILE_SIZE, player.getX(), 2);
		assertEquals((d.getY()+1)*World.TILE_SIZE, player.getY(), 2);
	}

	@Test
	public void testLockedDoorNoKey() {
		Door d = new Door("Door", 10, 10, 1000, null);
		d.setSendToY(2*World.TILE_SIZE);
		d.setNeedKey(true);

		Player player = new Player("",10*World.TILE_SIZE,11*World.TILE_SIZE,10);
		player.setFacing(Direction.NORTH);
		float xBefore = player.getX();
		float yBefore = player.getY();

		d.use(player);
		assertEquals(xBefore, player.getX(), 2);
		assertEquals(yBefore, player.getY(), 2);
	}

	@Test
	public void testLockedDoorWithKey() {
		Door d = new Door("Door", 10, 10, 1000, null);
		d.setSendToY(2*World.TILE_SIZE);
		d.setNeedKey(true);

		Player player = new Player("",10*World.TILE_SIZE,9*World.TILE_SIZE,10);
		player.setFacing(Direction.SOUTH);
		player.addToInventory(new KeyCard("Key", 0, 0, 0, null));

		d.use(player);
		assertEquals((d.getX())*World.TILE_SIZE, player.getX(), 2);
		assertEquals((d.getY()+1)*World.TILE_SIZE, player.getY(), 2);
	}

	@Test
	public void testPhaser() {
		Player player = new Player("",10*World.TILE_SIZE,9*World.TILE_SIZE,10);
		Phaser gun = new Phaser();
		for (int i = 0; i < 3; i++) {
			gun.use(player);
		}
		assertEquals(45, player.getBullets());
	}

	@Test
	public void testPill() {
		Player player = new Player("",10*World.TILE_SIZE,9*World.TILE_SIZE,10);
		Pill pill = new Pill("",0,0,0,10,null);
		pill.use(player);
		assertEquals(110, player.getHealth(), 2);
	}

	@Test
	public void testShieldOfAres() {
		Player player = new Player("",10*World.TILE_SIZE,9*World.TILE_SIZE,10);
		ShieldOfAres shield = new ShieldOfAres("Shield",0,0,0,null);

		assertEquals(0, shield.getAttack());
		assertEquals(50, shield.getDefence());

		shield.use(player);
		assertEquals(150, player.getHealth(), 2);
	}

	@Test
	public void testSonicPower() {
		SonicPower sonic = new SonicPower("Sonic",0,0,0);
		assertEquals(15, sonic.getAttack());
		assertEquals(0, sonic.getDefence());
		assertEquals(75, sonic.getDiameter());
	}

	@Test
	public void validSupplyBoxGetOut() {
		Player player = new Player("",10*World.TILE_SIZE,9*World.TILE_SIZE,10);
		SupplyBox sb = new SupplyBox("SupplyBox", 0, 0, 0, null);

		SonicPower sonic = new SonicPower("Sonic",0,0,0);
		sb.putIn(sonic);
		Pill pill = new Pill("",0,0,0,10,null);
		sb.putIn(pill);
		ShieldOfAres shield = new ShieldOfAres("Shield",0,0,0,null);
		sb.putIn(shield);
		Vest vest = new Vest("Vest",0,0,0, null);
		sb.putIn(vest);

		sb.getOut(player, 0);
		sb.getOut(player, 0);
		sb.getOut(player, 0);
		sb.getOut(player, 0);

		player.setCurrentItem(0);
		assertEquals(sonic, player.getItemAtHand());
		player.setCurrentItem(1);
		assertEquals(pill, player.getItemAtHand());
		player.setCurrentItem(2);
		assertEquals(shield, player.getItemAtHand());
		player.setCurrentItem(3);
		assertEquals(vest, player.getItemAtHand());
	}

	@Test
	public void invalidSupplyBoxGetOut() {
		Player player = new Player("",10*World.TILE_SIZE,9*World.TILE_SIZE,10);
		SupplyBox sb = new SupplyBox("SupplyBox", 0, 0, 0, null);

		SonicPower sonic = new SonicPower("Sonic",0,0,0);
		sb.putIn(sonic);
		Pill pill = new Pill("",0,0,0,10,null);
		sb.putIn(pill);
		ShieldOfAres shield = new ShieldOfAres("Shield",0,0,0,null);
		sb.putIn(shield);
		Vest vest = new Vest("Vest",0,0,0, null);
		sb.putIn(vest);

		//illegal index args
		sb.getOut(player, 1000);
		sb.getOut(player, -1000);
		sb.getOut(player, 4);
		sb.getOut(player, -1);

		assertTrue(player.getInventory().isEmpty());
	}

	@Test
	public void validSupplyBoxUse1() {
		Player player = new Player("",10*World.TILE_SIZE,9*World.TILE_SIZE,10);
		SupplyBox sb = new SupplyBox("SupplyBox", 0, 0, 0, null);

		SonicPower sonic = new SonicPower("Sonic",0,0,0);
		sb.putIn(sonic);
		Pill pill = new Pill("",0,0,0,10,null);
		sb.putIn(pill);
		ShieldOfAres shield = new ShieldOfAres("Shield",0,0,0,null);
		sb.putIn(shield);
		Vest vest = new Vest("Vest",0,0,0, null);
		sb.putIn(vest);

		sb.use(player);

		player.setCurrentItem(0);
		assertEquals(sonic, player.getItemAtHand());
		player.setCurrentItem(1);
		assertEquals(pill, player.getItemAtHand());
		player.setCurrentItem(2);
		assertEquals(shield, player.getItemAtHand());
		player.setCurrentItem(3);
		assertEquals(vest, player.getItemAtHand());
	}

	@Test
	public void validSupplyBoxUse2() {
		Player player = new Player("",10*World.TILE_SIZE,9*World.TILE_SIZE,10);
		SupplyBox sb = new SupplyBox("SupplyBox", 0, 0, 0, null);

		//put 10 shields
		ShieldOfAres shield = new ShieldOfAres("Shield", 10, 10, 10, null);
		for (int i = 0; i < 10; i++) {
			sb.putIn(shield);
		}
		//put 1 of each
		SonicPower sonic = new SonicPower("Sonic",0,0,0);
		sb.putIn(sonic);
		Pill pill = new Pill("",0,0,0,10,null);
		sb.putIn(pill);
		Vest vest = new Vest("Vest",0,0,0, null);
		sb.putIn(vest);

		//only 10 shields should be taken, other 3 items are left in SupplyBox
		sb.use(player);

		for (int i = 0; i < 10; i++) {
			player.setCurrentItem(i);
			assertEquals(shield, player.getItemAtHand());
		}
	}

	@Test
	public void emptySupplyBoxUse() {
		Player player = new Player("",10*World.TILE_SIZE,9*World.TILE_SIZE,10);
		SupplyBox sb = new SupplyBox("SupplyBox", 0, 0, 0, null);

		//put DIRECTLY in player's inventory
		SonicPower sonic = new SonicPower("Sonic",0,0,0);
		player.addToInventory(sonic);
		Pill pill = new Pill("",0,0,0,10,null);
		player.addToInventory(pill);
		Vest vest = new Vest("Vest",0,0,0, null);
		player.addToInventory(vest);

		//3 items in player's inventory
		assertEquals(3, player.getInventory().size());

		//empty SupplyBox
		sb.use(player);

		//STILL, 3 items in player's inventory
		assertEquals(3, player.getInventory().size());
	}

	@Test
	public void testVest() {
		Player player = new Player("",10*World.TILE_SIZE,9*World.TILE_SIZE,10);
		Vest vest = new Vest("Vest",0,0,0, null);

		assertEquals(0, vest.getAttack());
		assertEquals(20, vest.getDefence());

		vest.use(player);
		assertEquals(120, player.getHealth(),2);
	}
}
