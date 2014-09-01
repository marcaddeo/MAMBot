package mamclient.GameObjects;

import mamclient.Packets.ItemInfoPacket;

public class Item {

	public boolean equipped;
	
	public String name;
	public String maker;
	
	public int level;
	public int id;
	public int ownerId;
	
	public short look;
	public short itemType;
	public short life;
	public short mana;
	public short attack;
	public short defense;
	public short dexterity;
	
	public Item() {	
	}
	
	public Item(ItemInfoPacket itemPacket) {	
		this.equipped			= itemPacket.equipped;
		this.ownerId			= itemPacket.ownerId;   
		this.name				= itemPacket.name;	   
		this.maker 				= itemPacket.maker;	   
		this.id					= itemPacket.id;		   
		this.look				= itemPacket.look;		   
		this.itemType 			= itemPacket.itemType;	   
		this.level 				= itemPacket.level;	   
		this.life 				= itemPacket.life;		   
		this.mana 				= itemPacket.mana;		   
		this.attack 			= itemPacket.attack;	   
		this.defense 			= itemPacket.defense;	   
		this.dexterity 			= itemPacket.dexterity;
	}
}
