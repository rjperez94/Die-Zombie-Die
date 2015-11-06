package game.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Server implementation
 *
 * Based on
 * http://tutorials.jenkov.com/java-multithreaded-servers/multithreaded-server.html
 * @author gordon
 *
 */
public class Server implements Runnable{

	public static final int TIMEOUT = 0;

	private int MAX_CONNECTIONS = 4;
	private int 							port = 8008;
	private ServerSocket 					serverSocket;

	private Thread							serverThread;
	protected Thread 						runningThread;
	protected Thread						updatorThread;

	private ArrayBlockingQueue<Request> 	updates;//syncronised queue of jobs
	private HashMap<String, Connection>		clients;
	private byte[]							clientNum = new byte[MAX_CONNECTIONS];

	private boolean							isStopped = true;

	//debug variables
	private long 							debugStartTime;


	// TODO Add GameWorld class when it is made for communication

	/**
	 * Creates a new Server, given a port
	 * @param port - The server Port
	 */
	public Server(int port,int maxConnection){
		this.port = port;
		this.MAX_CONNECTIONS = maxConnection;
		init();
	}

	/**
	 * Creates a new Server, with the default port of 8008 <-hehe B00B
	 */
	public Server(){
		init();
	}

	private void init(){
		this.clients = new HashMap<>();
		this.updates = new ArrayBlockingQueue<>(1000);
		for(int i = 0; i < MAX_CONNECTIONS; i++){
			clientNum[i] = 0;
		}
	}

	@Override
	public void run() {
		showMessage("Running");
		synchronized(this){
			this.runningThread = Thread.currentThread();
		}
		openServerSocket();

		this.updatorThread = new UpdateThread(this);
		this.updatorThread.start();

		this.isStopped = false;

		while(!this.isStopped()){
			acceptConnection();
		}




	}
	/**
	 * Accepts a connection, sets up the needed socket, and dedicates a new thread
	 */
	private void acceptConnection() {
		Socket clientSocket = null;
		try {
			clientSocket = this.serverSocket.accept();
		} catch (IOException e) {
			showMessage("Client Connect Error: " + e.getMessage());
			return;
		}
		int playerNum = getPlayerNum();

		/**
		 * In Event of Server Full
		 */
		if(playerNum < 0){
			showMessage("Server Full");
			try {
				clientSocket.close();
			} catch (IOException e) {
			}
			return;
		}

		String userName = "Player" + playerNum;
		new Thread(
			new ServerWorker(userName, clientSocket, this)
		).start(); //HOW DID I FOGET TO USE .run(), then promptly forget i am supposed to use .start()
		clients.put(userName, new Connection(clientSocket));
		showMessage("Client connected: " + userName);

	}

	/**
	 * Gets an available slot from 1 to MAX_CONNECTIONS
	 * @return
	 */
	private int getPlayerNum() {
		for(int i = 0; i < MAX_CONNECTIONS; i++){
			if(clientNum[i] == 0){
				clientNum[i] = 1;
				return i +1 ;
			}
		}
		return -1;
	}

	public void disconnect(String userName){
		clients.remove(userName);
		int num = Integer.parseInt(userName.substring(userName.length()-1));
		clientNum[num-1] = 0;
	}

	private synchronized boolean isStopped() {
        return this.isStopped;
    }

	/**
	 * Creates an open socket for clients to connect to
	 */
	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(port);
			serverSocket.setPerformancePreferences(1, 20, 0);
		} catch (IOException e) {
			showMessage("Can not open port " + this.port + " is it already being used?\n " + e.getMessage());
		}

	}
	/**
	 * Returns a BlockingQueue that can be used to add a new Request
	 * @return
	 */
	public ArrayBlockingQueue<Request> getUpdateQueue(){
		return this.updates;
	}

	public synchronized boolean stop(){
		try {
			this.serverSocket.close();
			isStopped = true;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * Starts the server thread.
	 */
	public synchronized void start() {
		serverThread = new Thread(this);
		serverThread.start();
	}

	/**
	 * Returns a Client connection from the server
	 * @param userName the user name to look up
	 * @return A Client Connection
	 */
	public Connection getClient(String userName){
		return this.clients.get(userName);
	}

	public ArrayList<Connection> getAllConnections(){
		return new ArrayList<>(this.clients.values());
	}

	/**
	 * Shows an info message to the screen
	 * @param message
	 */
	protected void showMessage(final String message){
		System.out.println(message);
		//TODO THread.invokeLater on appending to screen
	}

	public void setDebugTime(String action){
		this.debugStartTime = System.currentTimeMillis();
		showMessage("Debugging: " + action);
	}

	public void stopDebugTime(){
		long end = System.currentTimeMillis();
		showMessage("Delta Time: " + (end - this.debugStartTime));
	}

}
