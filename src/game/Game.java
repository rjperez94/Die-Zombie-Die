package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import game.exception.GraphicsException;
import game.graphics.GraphicsLoader;
import game.input.KeyHandler;
import game.state.GameStateManager;

/**
 * <code>Game</code> holds the main entry point for the client side of the game.
 * <p>
 * This class holds an <code>update</code> method to allow the updating of game
 * logic, as well as a <code>render</code> method, allowing all required
 * graphics to be drawn to the screen.
 * 
 * @author Brendan Goodenough
 * @version 0.1.0
 */

@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable {
	private BufferedImage image;
	private GameStateManager gsm;
	private Font font;

	private Thread thread;
	private boolean running;
	private int fps = 60;

	public static final int WIDTH = 320;
	public static final int HEIGHT = 180;
	public static final int SCALE = 4;

	public static final String TITLE = "Die Zombie Die";
	public static final boolean DEBUG = true;

	/**
	 * This constructor sets the size of the <code>Canvas</code> and creates a
	 * new <code>JFrame</code> for the game.
	 */
	public Game() {
		image = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		gsm = new GameStateManager();
		
		try {
			font = GraphicsLoader.loadFont("/fonts/gamegirl.ttf");
		} catch (GraphicsException e) {
			e.printStackTrace();
		}

		setPreferredSize(new Dimension(Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE));

		JFrame frame = new JFrame(Game.TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		addKeyListener(new KeyHandler(gsm));
		requestFocus();
	}

	/**
	 * Updates the current <code>GameState</code> by updating the
	 * <code>GameStateManager</code>.
	 */
	public void update() {
		gsm.update();
	}

	/**
	 * Draws graphics for the current <code>GameState</code> to the screen by
	 * first clearing the screen and drawing any graphics to a
	 * <code>BufferedImage</code> which is then scaled it to match the size of
	 * the <code>Canvas</code>.
	 * <p>
	 * If a <code>BufferStrategy</code> does not exist for the
	 * <code>Canvas</code>, this method will create a
	 * <code>BufferStrategy</code> with triple buffering enabled.
	 */
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3); // triple buffering
			return;
		}

		Graphics g = bs.getDrawGraphics();
		Graphics2D g2 = (Graphics2D) image.getGraphics();

		g2.setFont(font);
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

		gsm.render(g2);

		g.drawImage(image, 0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE, null);
		if (Game.DEBUG) {
			g.setColor(Color.WHITE);
			g.drawString(fps + "FPS", 10, 20);
		}
		g.dispose();
		bs.show();
	}

	/**
	 * Controls the main gameloop by limiting the amount of updating and
	 * rendering that can occur every second.
	 * <p>
	 * While the game is running, the <code>update</code> and
	 * <code>render</code> methods will be called at a desired rate of 60 times
	 * per second. This limited updating and rendering provides an average speed
	 * at which the game is updated and prevents unwanted stuttering.
	 */
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double ns = 1000000000D / 60D;
		double delta = 0;
		int frames = 0;

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1) {
				update();
				render();
				delta--;
				frames++;
			}

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				fps = frames;
				frames = 0;
			}
		}
	}

	/**
	 * Starts the gameloop by initializing and starting a new
	 * <code>Thread</code> that will allow both updating and rendering for the
	 * game.
	 */
	private synchronized void start() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	/*
	 * Main entry point for the client.
	 */
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
}
