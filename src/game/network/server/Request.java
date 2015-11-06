package game.network.server;

import game.events.Event;

public class Request {
	private String client;
	private Event event;
	
	
	public Request(String client, Event event) {
		this.client = client;
		this.event = event;
	}


	public String getClient() {
		return client;
	}


	public Event getEvent() {
		return event;
	}
	
	
	

}
