package game.world;

import java.awt.Color;
import java.awt.Graphics2D;

import game.Game;
import game.character.Player;

/**
 * <code>MiniMap</code> is designed to provide our second view for the game.
 * This aspect shows a fully top down approach as opposed to the actual game
 * 
 * 
 * @author Brendan Goodenough
 * @version 0.1.0
 */

public class MiniMap {
	private World world;
	private Player player;

	public MiniMap(World world, Player player) {
		this.world = world;
		this.player = player;
	}

	public void update() {

	}

	public void render(Graphics2D g2) {
		final int x = Game.WIDTH - World.WIDTH - 5;
		final int y = 5;

		g2.setColor(new Color(255, 255, 255, 120));
		g2.fillRect(x - 5, y - 5, World.WIDTH + 10, World.HEIGHT + 10);

		for (GameObject object : world.getObjects()) {
			g2.setColor(Color.BLACK);
			g2.fillRect(x + (int) object.getX(), y + (int) object.getY(), 1, 1);
		}

		for (game.character.Character character : world.getEntities()) {
			g2.setColor(Color.RED);
			g2.fillRect(x + (int) Math.ceil(character.getX() / World.TILE_SIZE), y + (int) Math.ceil(character.getY() / World.TILE_SIZE), 1, 1);
		}
		
		g2.setColor(Color.BLUE);
		g2.fillRect(x + (int) Math.ceil(player.getX() / World.TILE_SIZE), y + (int) Math.ceil(player.getY() / World.TILE_SIZE), 1, 1);
	}
}
