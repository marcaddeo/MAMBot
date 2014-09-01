package mamclient.Packets;

import mamclient.Event;
import csocket.Packet;

public interface IPacket extends Event {

	public Packet pack();
	public void parse(Packet packet);
}
