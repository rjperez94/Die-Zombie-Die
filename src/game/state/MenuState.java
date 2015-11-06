package game.state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import game.exception.GameException;
import game.exception.GraphicsException;
import game.graphics.GraphicsLoader;
import game.graphics.Sprite;
import game.graphics.StaticSprite;
import game.sound.AudioClip;

/**
 * MenuState contains all the logic and rendering for the menu of the game.
 *
 * @author Brendan Goodenough
 * @version 0.1.0
 */

public class MenuState extends GameState {
	private Sprite starfield;
	private boolean accelerating, decelerating;
	private float vel, xOffset;

	private int screen, selectedMainMenuOption, selectedMultiplayerOption,
			selectedJoinOption;
	private String[] mainMenuOptions = { "Play", "Multiplayer", "Quit" };
	private String[] multiplayerMenuOptions = { "Join", "Host", "Back" };
	private String[] joinOptions = { "Host: localhost", "Port: 8008", "Join",
			"Back" };
	private String host = "localhost", port = "8008";

	private AudioClip bgm;

	/**
	 * Passes the GameStateManager object to the parent GameState class.
	 *
	 * @param gsm
	 *            - GameStateManager object
	 */
	public MenuState(GameStateManager gsm) {
		super(gsm);
	}

	/**
	 * Initializes all required objects and variables in the state.
	 */
	@Override
	public void init() {
		bgm = new AudioClip("/sounds/fight.wav");
		vel = 1f;

		try {
			starfield = new StaticSprite(
					GraphicsLoader.loadImage("/backgrounds/menu.png"));
		} catch (GraphicsException e) {
			e.printStackTrace();
		}
		bgm.start();
	}

	/**
	 * Updates all game logic for the state.
	 */
	@Override
	public void update() {
		starfield.setX(starfield.getX() - vel);
		if (starfield.getX() <= -starfield.getWidth()) {
			starfield.setX(0);
		}

		if (screen == 1) {
			if (accelerating) {
				if (vel < 20f) {
					vel += 0.8f;
				} else {
					accelerating = false;
					decelerating = true;
				}
			} else if (decelerating) {
				if (vel > 1f) {
					vel -= 0.8f;
				} else {
					vel = 1f;
					decelerating = false;
				}
			}

			if (xOffset > -1000) {
				xOffset -= 25f;
			} else if (xOffset < -1000) {
				xOffset = -1000f;
			}
		} else if (screen == 2) {
			if (accelerating) {
				if (vel < 20f) {
					vel += 0.8f;
				} else {
					accelerating = false;
					decelerating = true;
				}
			} else if (decelerating) {
				if (vel > 1f) {
					vel -= 0.8f;
				} else {
					vel = 1f;
					decelerating = false;
				}
			}

			if (xOffset > -2000) {
				xOffset -= 25f;
			} else if (xOffset < -2000) {
				xOffset = -2000f;
			}
		} else if (screen == 0) {
			if (accelerating) {
				if (vel < 20f) {
					vel += 0.8f;
				} else {
					accelerating = false;
					decelerating = true;
				}
			} else if (decelerating) {
				if (vel > 1f) {
					vel -= 0.8f;
				} else {
					vel = 1f;
					decelerating = false;
				}
			}

			if (xOffset < 0f) {
				xOffset += 25f;
			} else if (xOffset > 0f) {
				xOffset = 0f;
			}
		}
	}

	/**
	 * Renders all graphics for the state.
	 *
	 * @param g2
	 *            - graphics object
	 */
	@Override
	public void render(Graphics2D g2) {
		starfield.render(g2);
		starfield.render(g2, starfield.getX() + starfield.getWidth(), 0);

		g2.setColor(Color.WHITE);
		g2.drawString("Die Zombie Die", 40, 40);

		for (int i = 0; i < mainMenuOptions.length; i++) {
			if (i == selectedMainMenuOption) {
				g2.setColor(new Color(240, 60, 60));
			} else {
				g2.setColor(Color.WHITE);
			}
			g2.drawString(mainMenuOptions[i], 40 + (int) xOffset, 80 + (i * 18));
		}

		for (int i = 0; i < multiplayerMenuOptions.length; i++) {
			if (i == selectedMultiplayerOption) {
				g2.setColor(new Color(240, 60, 60));
			} else {
				g2.setColor(Color.WHITE);
			}
			g2.drawString(multiplayerMenuOptions[i], 1040 + (int) xOffset,
					80 + (i * 18));
		}

		for (int i = 0; i < joinOptions.length; i++) {
			if (i == selectedJoinOption) {
				g2.setColor(new Color(240, 60, 60));
			} else {
				g2.setColor(Color.WHITE);
			}
			g2.drawString(joinOptions[i], 2040 + (int) xOffset, 80 + (i * 18));
		}
	}

	/**
	 * Handles any key press events that are passed through.
	 *
	 * @param key
	 *            - key code
	 */
	@Override
	public void keyPressed(int key) {
		boolean keyFound = false;

		if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) {
			keyFound = true;
			try {
				if (screen == 0) {
					switch (selectedMainMenuOption) {
					case 0:
						bgm.stop();
						gsm.setCurrentState(GameStateManager.PLAY_STATE);
						break;
					case 1:
						screen = 1;
						accelerating = true;
						break;
					case 2:
						System.exit(0);
						break;
					}
				} else if (screen == 1) {
					switch (selectedMultiplayerOption) {
					case 0:
						screen = 2;
						accelerating = true;
						break;
					case 1:
						//new ServerMenu();
						break;
					case 2:
						screen = 0;
						accelerating = true;
						break;
					}
				} else if (screen == 2) {
					switch (selectedJoinOption) {
					case 2:
						bgm.stop();
						Multiplayer mp = (Multiplayer) gsm
								.getState(GameStateManager.MULTIPLAYER);
//						mp.setHost(host,Integer.parseInt(port));
						gsm.setCurrentState(GameStateManager.MULTIPLAYER);
						break;
					case 3:
						screen = 1;
						break;
					}
				}
			} catch (GameException e) {
			}
		}

		if (key == KeyEvent.VK_UP) {
			keyFound = true;
			if (screen == 0) {
				selectedMainMenuOption--;

				if (selectedMainMenuOption < 0) {
					selectedMainMenuOption = mainMenuOptions.length - 1;
				}
			} else if (screen == 1) {
				selectedMultiplayerOption--;

				if (selectedMultiplayerOption < 0) {
					selectedMultiplayerOption = multiplayerMenuOptions.length - 1;
				}
			} else if (screen == 2) {
				selectedJoinOption--;

				if (selectedJoinOption < 0) {
					selectedJoinOption = joinOptions.length - 1;
				}
			}
		}

		if (key == KeyEvent.VK_DOWN) {
			keyFound = true;
			if (screen == 0) {
				selectedMainMenuOption++;

				if (selectedMainMenuOption >= mainMenuOptions.length) {
					selectedMainMenuOption = 0;
				}
			} else if (screen == 1) {
				selectedMultiplayerOption++;

				if (selectedMultiplayerOption >= multiplayerMenuOptions.length) {
					selectedMultiplayerOption = 0;
				}
			} else if (screen == 2) {
				selectedJoinOption++;

				if (selectedJoinOption >= joinOptions.length) {
					selectedJoinOption = 0;
				}
			}
		}

		if (!keyFound) {
			if (screen == 2 && selectedJoinOption == 0) {
				if (key == KeyEvent.VK_BACK_SPACE) {
					try {
						host = host.substring(0, host.length() - 1);
					} catch (StringIndexOutOfBoundsException e) {
					}
				} else {
					host += (char) key;
				}
				joinOptions[0] = "Host: " + host;
			} else if (screen == 2 && selectedJoinOption == 1) {
				if (key == KeyEvent.VK_BACK_SPACE) {
					try {
						port = port.substring(0, port.length() - 1);
					} catch (StringIndexOutOfBoundsException e) {
					}
				} else {
					port += (char) key;
				}
				joinOptions[1] = "Port: " + port;
			}
		}
	}

	/**
	 * Handles any key release events that are passed through.
	 *
	 * @param key
	 *            - key code
	 */
	@Override
	public void keyReleased(int key) {
	}

}
