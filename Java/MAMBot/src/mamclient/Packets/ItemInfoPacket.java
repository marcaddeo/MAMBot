package mamclient.Packets;

import mamclient.MAMClient;
import mamclient.GameObjects.Item;
import csocket.Packet;

public class ItemInfoPacket implements IPacket {
	
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
	
	public ItemInfoPacket() {	
	}
	
	public ItemInfoPacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	public ItemInfoPacket(Packet packet) {
		this.parse(packet);
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		client.inventory.addItem(new Item(new ItemInfoPacket(packet)));
		client.emit("receiveitem", packet);
		
		/*System.out.printf("name: %s; maker: %s; id: %d; level: %d; Equipped: %s;\n"
				+ "life: %d; mana: %d; attack: %d; defense: %d; dexterity: %d;\n",
				this.name, this.maker, this.id, this.level, (this.equipped) ? "true" : "false",
				this.life, this.mana, this.attack, this.defense, this.dexterity);*/
	}

	@Override
	public Packet pack() {
		return new Packet(0, 0);
	}
	
	@Override
	public void parse(Packet packet) {
		int lev			= packet.getInt(52);
		this.equipped 	= (packet.getInt(0) != 3003) ? true : false;
		this.ownerId 	= packet.getInt(4);
		this.name		= packet.getString(8, 16);
		this.maker		= packet.getString(24, 16);
		this.id			= packet.getInt(40);
		this.look		= packet.getShort(48);
		this.itemType	= packet.getShort(50);
		this.level		= (lev > 1400) ? lev - 1000000 : lev;
		this.life		= packet.getShort(56);
		this.mana		= packet.getShort(58);
		this.attack		= packet.getShort(60);
		this.defense	= packet.getShort(62);
		this.dexterity	= packet.getShort(64);
	}

}
