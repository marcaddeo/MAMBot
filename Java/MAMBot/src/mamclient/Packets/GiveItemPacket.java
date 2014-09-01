package mamclient.Packets;

import mamclient.MAMClient;
import csocket.Packet;

public class GiveItemPacket implements IPacket {
	
	public int playerId;
	public int target;
	public int action;
	public int itemId;
	
	public GiveItemPacket() {
	}
	
	public GiveItemPacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	public GiveItemPacket(Packet packet) {
		this.parse(packet);
	}
	
	public GiveItemPacket(int playerId, int targetId, int itemId, int itemType) {
		this.playerId 	= playerId;
		this.target 	= targetId;
		this.itemId		= itemId;
		this.action 	= itemType;
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);
	}

	@Override
	public Packet pack() {
		Packet packet = new Packet(20, 1012);
		
		packet.putInt(this.playerId, 0);
		packet.putInt(this.target, 4);
		packet.putInt(this.action, 8);
		packet.putInt(this.itemId, 12);
		
		return packet;
	}

	@Override
	public void parse(Packet packet) {
		// TODO I don't think I would ever receive this packet?
		// If I do, I don't see why it would matter..
	}

}
