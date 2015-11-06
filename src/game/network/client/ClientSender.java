package game.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import game.events.Event;

public class ClientSender {
	private ObjectOutputStream 	output;

	private String 						serverIP;
	private Socket 						connection = null;
	private int 						PORTNUM = 8008;
	private ArrayBlockingQueue<Event>	events;
	private ClientListener				listener;



	/**
	 * Constructs a new Client object that can be used to communicate
	 * to a server
	 * @param serverIP The INET address
	 * @param portNumber The open port
	 */
	public ClientSender(String serverIP, int portNumber){
		this.serverIP = serverIP;
		this.PORTNUM = portNumber;
		init();
	}

	/**
	 * Constructs a new Client object that can be used to communicate
	 * to a server, with the default port of 8008
	 * @param serverIP The INET address
	 */
	public ClientSender(String serverIP){
		this.serverIP = serverIP;
		showMessage("new Client created");
		init();
	}

	private void init(){
		this.events = new ArrayBlockingQueue<>(200);
		try {
			connectToServer();
			setupStreams();
			showMessage("Conected to server");
			listener = new ClientListener(new ObjectInputStream(connection.getInputStream()), this);
			
			new Thread(listener).start();
		} catch (IOException e) {
			showMessage("Client connect failure");
			closeConnection();
		}
	}

	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();

	}

	private void connectToServer() throws IOException {
		connection = new Socket(InetAddress.getByName(serverIP), PORTNUM);
		connection.setTcpNoDelay(true);
		connection.setPerformancePreferences(1, 20,0);
	}

	public boolean send(Event e){
		if(connection != null){
			try {
				output.writeObject(e);
				output.flush();
				showMessage("Sent Object");
				return true;
			} catch (IOException e1) {
				showMessage("Send fail");
				e1.printStackTrace();
				closeConnection();
			}
		}
		return false;
	}

	public ArrayBlockingQueue<Event> getEvents() {
		return events;
	}

	/**
	 * Returns all events this client has received, at the time of calling this method
	 * @return An ArrayList of events
	 */
	public ArrayList<Event> getCurrentEvents(){
		ArrayList<Event> list = new ArrayList<>();
		events.drainTo(list);
		return list;
	}

	/**
	 * Returns the first event in the Client buffer
	 * @return A Single Event
	 */
	public Event poll(){
		return events.poll();
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
		
		try {
			listener.closeConnection();
			output.close();
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
