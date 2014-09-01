package mamclient.Packets;

import mamclient.MAMClient;
import csocket.Packet;

public class NpcClickDialoguePacket implements IPacket {

	public int option;
	
	public NpcClickDialoguePacket(int option) {
		this.option = option;
	}
	
	public NpcClickDialoguePacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	public NpcClickDialoguePacket(Packet packet) {
		this.parse(packet);
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);
		// We should never receive this packet
	}

	@Override
	public Packet pack() {
		Packet packet = new Packet(12, 2032);
		
		packet.putInt(this.option, 0);
		packet.putByte((byte) 100, 4);
		
		return packet;
	}

	@Override
	public void parse(Packet packet) {
		// Should never receive this packet
	}

}
