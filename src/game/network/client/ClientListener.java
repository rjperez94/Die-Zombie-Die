package game.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import game.events.Event;

public class ClientListener extends Thread{

	private ObjectInputStream input;
	private ClientSender client;
	private boolean run = true;

	public ClientListener(ObjectInputStream input, ClientSender client) {
		this.input = input;
		this.client = client;

		setDaemon(true);
	}

	@Override
	public void run() {
		while(run){
			try {
				Event event = (Event) input.readObject();
				client.getEvents().add(event);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				showMessage("Class not found: " + e.getMessage());

			} catch (IOException e){
				e.printStackTrace();
				showMessage("IOException: " + e.getMessage());
				client.closeConnection();;
			}
		}
	}

	/**
	 * Shows an info message to the screen
	 * @param message
	 */
	protected void showMessage(final String message){
		System.out.println(message);
		//TODO THread.invokeLater on appending to screen
	}
	
	
	public void closeConnection(){
		this.run = false;
		try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
