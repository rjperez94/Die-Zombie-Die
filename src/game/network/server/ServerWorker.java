package game.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import game.events.*;

/**
 * A thread designed to handle each connection, although an older design has now been determined to have
 * a greater performance
 * http://paultyma.blogspot.com/2008/03/writing-java-multithreaded-servers.html
 * @author gordon
 *
 */
public class ServerWorker extends Thread{
	
	
	protected Socket 			clientSocket = null;
	protected Server 			server;
	protected boolean 			isStopped = true;
	protected String			userName;
	
	private ObjectInputStream 	in;
	
	
	/**
	 * Creates a new Worker to handle a communication with a new connection
	 * @param userName String - The clients userName
	 * @param client Socket - The clients socket
	 * @param server Server - The Host Server
	 */
	public ServerWorker(String userName, Socket client, Server server) {
		this.clientSocket = client;
		this.server = server;
		this.userName = userName;
		
		/**
		 * makes the thread a Daemon thread this prevents the thread from stopping
		 * the JVM from closing
		 * http://stackoverflow.com/a/10546740
		 */
		this.setDaemon(true);
	}

	/**
	 * Start the thread communicating with the client
	 */
	public void run() {
		try {
			setupStreams();
			isStopped = false;
			while(!isStopped()){
				startComunicating();				
			}
			
		} catch (IOException e) {
			server.showMessage("Connection Setup Failure: " + e.getMessage());
		}finally{
			closeComms();
		}
		
	}
	
	/**
	 * Safely closes the communication lines
	 */
	private void closeComms() {
		try {
			in.close();
			clientSocket.close();
			server.disconnect(this.userName);
		} catch (IOException e) {
			server.showMessage("Close Failure: " + e.getMessage());
		}
		
	}

	/**
	 * Accepts one input from the connected client and pushes it to the Server
	 */
	private void startComunicating() {
		try {
			Event event = (Event) in.readObject();
			server.setDebugTime("Pulled from client");
			if(event instanceof ServerClose){
				this.isStopped = true;
				return;
			}
			//Reads the event sent from the client, creates a new request and adds that to the process queue
			Request req = new Request(userName, event);
			while(!server.getUpdateQueue().offer(req));
			
		} catch (ClassNotFoundException e) {
			System.out.println("Invalid input: " + e.getMessage());
		} catch(IOException e){
			System.out.println("Connection lost: " + e.getMessage());
			this.isStopped = true;
			
		}
		
	}

	/**
	 * Creates the streams for communication between the worker and the client
	 * @throws IOException
	 */
	private void setupStreams() throws IOException {
		this.clientSocket.setSoTimeout(Server.TIMEOUT);
		in = new ObjectInputStream(clientSocket.getInputStream());
		
	}

	/**
	 * Checks if the thread is active
	 * @return boolean - If the thread is active
	 */
	private synchronized boolean isStopped() {
        return this.isStopped;
    }

}
