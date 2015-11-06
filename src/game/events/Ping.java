package game.events;

import java.io.Serializable;

public class Ping implements Event, Serializable {
	
	private static final long serialVersionUID = 1217101995;
	private long time;

	public Ping(long time) {
		this.time = time;
	}

	public long getTime() {
		return time;
	}
	
	
	
	

}
