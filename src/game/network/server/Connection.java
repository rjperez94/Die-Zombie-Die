package game.network.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {
	
	private Socket socket;
	private ObjectOutputStream output;
	
	public Connection(Socket socket){
		this.socket = socket;
		try {
			this.output = new ObjectOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void sendObject(Object ob) throws IOException{
		output.writeObject(ob);
		output.flush();
	}

}
