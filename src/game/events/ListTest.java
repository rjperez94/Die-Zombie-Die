package game.events;

import java.io.Serializable;
import java.util.ArrayList;

public class ListTest implements Event, Serializable {
	private static final long serialVersionUID = 4655452411133936552L;
	private ArrayList<String> list;
	
	public ListTest(){
		this.list = new ArrayList<>();
		this.list.add("HELLO");
		this.list.add("WORLD");
		this.list.add("THIS");
		this.list.add("IS");
		this.list.add("WALDO");
		
	}
	
	public ArrayList<String> getList(){
		return this.list;
	}

}
