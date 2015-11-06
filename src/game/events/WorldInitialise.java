package game.events;

import game.character.Character;
import game.character.Player;
import game.item.type.Item;
import game.world.Bullet;
import game.world.GameObject;

import java.io.Serializable;
import java.util.List;

public class WorldInitialise implements Event, Serializable{
	private static final long serialVersionUID = -344336652652825480L;
		private List<Character> entities;
		private List<GameObject> stationaryObj;
		private List<Item> pickableItems;
		private List<Bullet> bullets;
		private GameObject[][] objects;

		public WorldInitialise (List<Player> p, GameObject[][] objects,
				List<Character> entities, List<GameObject> stationaryObj,
				List<Item> pickableItems, List <Bullet> bullets) {
			this.entities = entities;
	        this.stationaryObj = stationaryObj;
	        this.pickableItems = pickableItems;
	        this.bullets = bullets;
	        this.objects = objects;
		}

		public WorldInitialise() {
			// TODO Auto-generated constructor stub
		}

		public List<Character> getEntities() {
			return entities;
		}

		public List<GameObject> getStationaryObj() {
			return stationaryObj;
		}

		public List<Item> getPickableItems() {
			return pickableItems;
		}

		public List<Bullet> getBullets() {
			return bullets;
		}
		
		public GameObject[][] getObjects() {
			return objects;
		}


}
