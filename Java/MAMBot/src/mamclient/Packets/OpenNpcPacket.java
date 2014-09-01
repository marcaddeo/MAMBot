package mamclient.Packets;

import mamclient.MAMClient;
import csocket.Packet;

public class OpenNpcPacket implements IPacket {
	
	public int id;
	
	public OpenNpcPacket(int id) {
		this.id = id;
	}
	 
	public OpenNpcPacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}

	public OpenNpcPacket(Packet packet) {
		this.parse(packet);
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);
		// We shouldn't ever receive these packets...
	}

	@Override
	public Packet pack() {
		Packet packet = new Packet(16, 2031);
		
		packet.putInt(this.id, 0);
		
		return packet;
	}

	@Override
	public void parse(Packet packet) {
		// never receive this packet
	}
	
}
