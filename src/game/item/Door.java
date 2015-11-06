package game.item;

import java.io.Serializable;

import game.Utilities.Direction;
import game.character.Player;
import game.graphics.Sprite;
import game.item.type.Item;
import game.state.PlayState;

/**
 * THis represents the Door. It connects to a valid X,Y position in the game map
 * @author Ronni Perez "perezronn"
 *
 */
public class Door extends Item implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -6685708804430615005L;
	private float sendToX,sendToY;
	//private PlayState state;
	private boolean needsKey;	//is locked door?

	/**
	 * Constructor
	 * @param state - the game level
	 * @param name - item alias
	 * @param xStart - initial x position
	 * @param yStart - initial y position
	 * @param zStart - initial z position
	 * @param sprite - item graphics
	 */
	//public Door (PlayState state, String name, float xStart, float yStart, float zStart, Sprite sprite) {
	public Door (String name, float xStart, float yStart, float zStart, Sprite sprite) {
		super(name, xStart, yStart, zStart, sprite);
		//this.state = state;
		this.sendToX = 0;
		this.sendToY = 0;
		this.needsKey = false;
	}

	@Override
	public void use(Player player) {
		//inform player if locked door
		if (needsKey) {
			System.out.println("LOCKED DOOR. Needs KeyCard");
		}
		//transfer Player if Door needs a keycard and Player has a keycard
		//transfer Player if Door does not need a keycard
		if ((this.needsKey && player.hasKey()) || !this.needsKey) {
			float playerX = player.getX();
			float playerY = player.getY();
			if(sendToY==0){				//go horizontally
				if(player.isFacing()==Direction.WEST){
					player.setX(playerX-sendToX);
					player.changeXOffsetTo(player.getXOffset()+sendToX);
				}
				else if(player.isFacing()==Direction.EAST){
					player.setX(playerX+sendToX);
					player.changeXOffsetTo(player.getXOffset()-sendToX);
				}
			} else if(sendToX==0){		//go vertically
				if(player.isFacing()==Direction.NORTH){
					player.setY(playerY-sendToY);
					player.changeYOffsetTo(player.getYOffset()+sendToY);
				}
				else if(player.isFacing()==Direction.SOUTH){
					player.setY(playerY+sendToY);
					player.changeYOffsetTo(player.getYOffset()-sendToY);

				}
			}else{
				System.out.println("Door Inactive");
			}

		}
	}

	//SETTERS
	public void setSendToY(float sendToY) {
		this.sendToY = sendToY;
	}

	public void setSendToX(float sendToX) {
		this.sendToX = sendToX;
	}

	public void setNeedKey (boolean b) {
		this.needsKey = b;
	}

	//GETTERS
	public boolean needsKey() {
		return needsKey;
	}
}
