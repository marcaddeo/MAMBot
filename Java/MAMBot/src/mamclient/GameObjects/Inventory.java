package mamclient.GameObjects;

import java.util.ArrayList;
import java.util.List;

// TODO rethink the inventory, add an equipment list seperate from the inventory. as well as a pet euip
// if i try to do for i = 0; i < itemCount; i++ it's just going to give me the equipped items first
// which fucks everything up
// fix this.. temporarily just made itemList public

public class Inventory {

	public List<Item> itemList;
	
	public int itemCount;
	
	public Inventory() {
		this.itemCount 	= 0;
		this.itemList 	= new ArrayList<Item>();
	}
	
	public Item at(int index) {
		if (index >= this.itemCount) return null; 
		return this.itemList.get(index);
	}
	
	public void addItem(Item item) {
		if (!item.equipped) this.itemCount++;
		this.itemList.add(item);
	}
	
	public void delItem(Item item) {
		if (this.itemList.contains(item)) {
			this.itemCount--;
			this.itemList.remove(item);
		}
	}
	
	public void delItem(int id) {
		this.delItem(this.findItem(id));
	}
	
	public Item findItem(int id) {
		for (Item item : this.itemList)
			if (item.id == id) return item;
		return null;
	}
	
	public Item findItem(String name) {
		for (Item item : this.itemList)
			if (item.name.equals(name)) return item;
		return null;
	}
	
}
