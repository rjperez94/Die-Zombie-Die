package game.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import game.state.GameStateManager;

/**
 * KeyHandler is designed to handle any key events from the user and pass the
 * key code through to the GameStateManager.
 * 
 * @author Brendan Goodenough
 */

public class KeyHandler extends KeyAdapter {
	private GameStateManager gsm;

	/**
	 * Constructs a new KeyHandler by assigning a GameStateManager object to use
	 * for passing through key events.
	 * 
	 * @param gsm - GameStateManager object
	 */
	public KeyHandler(GameStateManager gsm) {
		this.gsm = gsm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		gsm.keyPressed(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyAdapter#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		gsm.keyReleased(key);
	}
}
