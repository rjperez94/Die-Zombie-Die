package game.state;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import game.exception.GameException;

/**
 * GameStateManager is designed to be in control of all the possible game
 * states, deciding which state should be updated and rendered to the screen.
 * The possible states are stored in an ArrayList and can be accessed by getting
 * their index.
 */

public class GameStateManager {
	private List<GameState> states;
	private int currentState;

	public static final int MENU_STATE = 0;
	public static final int PLAY_STATE = 1;
	public static final int MULTIPLAYER = 2;

	/**
	 * Constructs a new GameStateManager by adding all states to an ArrayList
	 * and initializing the current state.
	 */
	public GameStateManager() {
		currentState = 0;

		states = new ArrayList<>();
		states.add(new MenuState(this));
		states.add(new PlayState(this));
		//states.add(new Multiplayer(this));
		states.get(currentState).init();
	}

	/**
	 * Updates the frame for the current state.
	 */
	public void update() {
		states.get(currentState).update();
	}

	/**
	 * Renders the frame for the current state.
	 *
	 * @param g2 - graphics object for rendering
	 */
	public void render(Graphics2D g2) {
		states.get(currentState).render(g2);
	}

	/**
	 * Gets the current state of the game.
	 *
	 * @return currentState
	 */
	public int getCurrentState() {
		return currentState;
	}

	/**
	 * Changes the current state that is being updated and rendered.
	 *
	 * @param currentState - new state for the game to run
	 */
	public void setCurrentState(int currentState) throws GameException {
		try {
			states.get(currentState).init();
			this.currentState = currentState;
		} catch (Exception e) {
			throw new GameException("Invalid state value");
		}
	}

	/**
	 * Handles a key press event by passing it to the current state.
	 *
	 * @param key - keycode of the pressed key
	 */
	public void keyPressed(int key) {
		states.get(currentState).keyPressed(key);
	}

	/**
	 * Handles a key release event by passing it to the current state.
	 *
	 * @param key - keycode of the released key
	 */
	public void keyReleased(int key) {
		states.get(currentState).keyReleased(key);
	}

	public GameState getState(int state){
		return states.get(state);
	}
}
