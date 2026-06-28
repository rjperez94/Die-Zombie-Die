import org.junit.Test;

import game.exception.GameException;
import game.state.GameStateManager;

import static org.junit.Assert.*;

/**
 * GraphicsTests are designed to test the GameStateManager and its states.
 */

public class GameStateTests {
	@Test
	public void testChangeValidGameState() {
		GameStateManager gsm = new GameStateManager();
		try {
			gsm.setCurrentState(1);
            assertEquals(1, gsm.getCurrentState());
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
			assertEquals(0, gsm.getCurrentState());
		}
	}
}
