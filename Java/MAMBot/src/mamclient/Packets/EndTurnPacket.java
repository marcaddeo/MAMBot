package mamclient.Packets;

import mamclient.MAMClient;
import csocket.Packet;

public class EndTurnPacket implements IPacket {

	@Override
	public void execute(MAMClient client, Packet packet) {
		client.emit("endturn");
	}

	@Override
	public Packet pack() {
		return null;
	}

	@Override
	public void parse(Packet packet) {
		// TODO I don't think there's any information in this packet, but it wouldn't hurt to check
	}

}
