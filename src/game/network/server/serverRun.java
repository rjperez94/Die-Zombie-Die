package game.network.server;

public class serverRun {
	
	/**
	 * TESTER METHOD
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(new Server(8008, 2)).run();
	}

}
