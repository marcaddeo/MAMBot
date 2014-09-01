package mamclient;

import csocket.Packet;

public class DummyEvent implements Event {

	@Override
	public void execute(MAMClient client, Packet packet) {
	}

}
