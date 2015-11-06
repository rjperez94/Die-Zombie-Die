package game.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import game.exception.GameException;
import game.state.GameStateManager;

/**
 * GraphicsTests are designed to test the GameStateManager and its states.
 */

public class GameStateTests {
	@Test
	public void testChangeValidGameState() {
		GameStateManager gsm = new GameStateManager();
		try {
			gsm.setCurrentState(1);
			assertTrue(gsm.getCurrentState() == 1);
		} catch (GameException e) {
			fail("Should change game state");
		}
	}

	@Test
	public void testChangeInvalidGameState() {
		GameStateManager gsm = new GameStateManager();
		try {
			gsm.setCurrentState(20);
			fail("Should not change game state");
		} catch (GameException e) {
			assertTrue(gsm.getCurrentState() == 0);
		}
	}
}
