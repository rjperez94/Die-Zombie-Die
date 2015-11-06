package game.network.client;

import java.util.ArrayList;

import game.events.*;
import game.world.World;

public class clientRun {

	/**
	 * TESTER METHOD
	 * @param args
	 */
	public static void main(String[] args) {
		ClientSender clint = new ClientSender("127.0.0.1");
		
		
		long start = System.currentTimeMillis();
		
		clint.send(new ListTest());
		
		Event ev = null;
		
		while(ev == null){
			ev = clint.poll();
		}
		
		if(ev instanceof ListTest){
			ListTest LS = (ListTest) ev;
			long end = System.currentTimeMillis();
			System.out.println("DELTA: " + (end-start));
			for(String s : LS.getList()){
				System.out.println(s);
			}
		}
		
		
		/*for(int i = 0; i < 20; i++){
			
			clint.send(new Ping(System.currentTimeMillis()));
			
		
			Event event = null;
			
			*//**
			 * Should probably use ClientSecer.getEvents() instead
			 * that way you dont need a while loop and you just
			 * get all updates
			 *//*
			while(event == null){
				event = clint.poll();
			}
			
			if(event instanceof Ping){
				Ping p = (Ping) event;
				System.out.println("Ping: " + (System.currentTimeMillis() - p.getTime()));
			}else if(false){
				// instance of WorldState
				//WorldState ws = (WorldState) event
				//ws.doSomething
			}//o other instances
		}*/
		
		
		
		


	}

}
