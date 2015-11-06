package game.state;

import java.awt.Graphics2D;

import game.state.GameStateManager;

/**
 * <code>GameState</code> allows for easy updating and rendering of each state
 * in the game as it allows all possible states in the game to be stored
 * together.
 * 
 * @author Brendan Goodenough
 * @version 0.1.0
 */

public abstract class GameState {
	protected GameStateManager gsm;

	/**
	 * This constructor sets the <code>GameStateManager</code> for the
	 * <code>GameState</code>. This allows the active <code>GameState</code> to
	 * easily terminate and switch to another state.
	 * 
	 * @param gsm <code>GameStateManager</code> object
	 */
	public GameState(GameStateManager gsm) {
		this.gsm = gsm;
	}

	/**
	 * Initializes the <code>GameState</code>.
	 */
	public abstract void init();

	/**
	 * Updates the <code>GameState</code> logic.
	 */
	public abstract void update();

	/**
	 * Draws the graphics of the <code>GameState</code> to the screen.
	 * 
	 * @param g2 <code>Graphics2D</code> object to use for rendering.
	 */
	public abstract void render(Graphics2D g2);

	/**
	 * Handles a key that has just been pressed.
	 * 
	 * @param key keycode of the pressed key
	 */
	public abstract void keyPressed(int key);

	/**
	 * Handles a key that has just been released.
	 * 
	 * @param key keycode of the pressed key
	 */
	public abstract void keyReleased(int key);
}
