package mamclient;

import csocket.Packet;

public interface Event {
	
	public void execute(MAMClient client, Packet packet);
	
}
