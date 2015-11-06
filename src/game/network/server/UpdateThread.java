package game.network.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import game.events.*;
import game.server.logic.ServerLogic;
import game.world.World;

/**
 * Runs an update to the game
 * @author gordon
 *
 */
public class UpdateThread extends Thread{

	Server 		server;
	ServerLogic sl;

	public UpdateThread(Server server) {
		this.server = server;
		this.sl = new ServerLogic(new World("/boards/mapFile.txt"));
		setDaemon(true);
	}

	@Override
	public void run() {

		while(true){
			Request update = server.getUpdateQueue().poll();

			if(update != null){
				Connection client = server.getClient(update.getClient());
				Event ev = update.getEvent();

				if(ev instanceof Ping){
					Ping ping = (Ping)ev;
					sendToClient(client, ping);
					server.stopDebugTime();
				}else if(ev instanceof ListTest){
					ListTest LT = (ListTest) ev;
					sendToClient(client, LT);
				} else if (ev instanceof MovePlayer) {
					sl.move((MovePlayer) ev);
					sendToALL (new AllPlayers(sl.getPlayers()));
				} else if (ev instanceof WorldInitialise) {
					sl.init((WorldInitialise) ev);
					for (int i = 0; i < sl.getWorld().getArray().length; i++) {
						for (int j = 0; j < sl.getWorld().getArray()[i].length; j++) {
							System.out.println(sl.getWorld().getArray()[i][j]);
						}
					}
					sendToClient(client, new WorldInitialise(sl.getWorld().getPlayers(), sl.getWorld().getArray(), sl.getWorld().getEntities(), sl.getWorld().getObjects(), sl.getWorld().getPickable(), sl.getWorld().getBullets()));
				}
			}

		}

	}

	/**
	 * Sends the event to the client in update
	 * @param update
	 */
	@Deprecated
	private void sendToClient(Request update){
		//TODO adjust to use connection and event rather than update, or overflow
		try {
			server.getClient(update.getClient()).sendObject(update.getEvent());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends the event to the client
	 * @param client The destination client
	 * @param eventToSend The event to send
	 */
	private void sendToClient(Connection client, Event eventToSend){
		try {
			client.sendObject(eventToSend);
		} catch (IOException e) {
			server.showMessage("Send failed: " + e.getMessage());
		}
	}

	/**
	 * Send the event to all connections on the server
	 * @param eventToSend THe event to send
	 */
	private void sendToALL(Event eventToSend){
		for(Connection c : server.getAllConnections()){
			try {
				c.sendObject(eventToSend);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
