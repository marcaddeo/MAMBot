package mamclient.Packets;

import mamclient.MAMClient;
import csocket.Packet;

public class NpcInfoPacket implements IPacket {

	public int id;
	
	public short type;
	public short look;
	public short x;
	public short y;
	
	public String name;
	
	public NpcInfoPacket() {
	}
	
	public NpcInfoPacket(Packet packet) {
		this.parse(packet);
	}
	
	public NpcInfoPacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);
		// TODO Add code to update the eventual NPC list that will be contained inside MAMClient
	}

	@Override
	public Packet pack() {
		return new Packet(0, 0);
	}

	@Override
	public void parse(Packet packet) {
		this.id 	= packet.getInt(0);
		this.type	= packet.getShort(6);
		this.look	= packet.getShort(8);
		this.x		= packet.getShort(10);
		this.y		= packet.getShort(12);
		this.name	= packet.getString(14, 16);
	}

}
