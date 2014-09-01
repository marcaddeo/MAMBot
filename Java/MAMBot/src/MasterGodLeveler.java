import csocket.Packet;
import mamclient.Event;
import mamclient.MAMClient;
import mamclient.Packets.MessagePacket;

class CommandParser implements Event {

	public MapMover mover;
	
	public CommandParser(MapMover mover) {
		this.mover = mover;
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		MessagePacket msg = new MessagePacket(packet);
		
		if (msg.message.equals("@level") && msg.sender.equals("jackyyll")) {
			MasterGodLeveler.start = System.currentTimeMillis();
			this.mover.move(131009);
		}
	}
	
}

class TestChecker implements Event {

	public MapMover mover;
	
	public TestChecker(MapMover mover) {
		this.mover = mover;
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		
		if (client.user.mapId == 131009) {
			switch (client.user.level) {
				case 299:
				case 599:
				case 899:
				case 1199:
					this.mover.move(100076);
					break;
				
				case 800:
					long elapsed = System.currentTimeMillis() - MasterGodLeveler.start;
					float minutes = elapsed / (60 * 1000F);
					System.err.printf("Done in %f min\n", minutes);
					break;
				
				default:
					client.battle.enter();
					break;
			}
		} else if (client.user.mapId == 100076)
			this.mover.move(131009);
	}
}

public class MasterGodLeveler {

	public MAMClient 	client;
	public MapMover 	mover;
	
	public static long start = 0;
	
	public MasterGodLeveler(MAMClient client) {
		this.client = client;
		this.mover	= new MapMover(this.client);
		
		client.on("1040", new Event() {
			@Override
			public void execute(MAMClient client, Packet packet) {
				if (packet.getInt(0) == client.user.characterId &&
						packet.getInt(8) == 14) {
					client.user.level = (short) packet.getInt(12);
				}
			}
		});
		
		client.on("message", new CommandParser(this.mover));
		
		client.on("maploaded", new Event() {
			@Override
			public void execute(MAMClient client, Packet packet) {

				switch (client.user.mapId) {
				
					case 100076:
						client.user.openNpc(100040);
						client.user.clickDialogue(1);
						break;
						
					case 131009:
						if (client.user.level >= 600 )
							client.user.jump((short) 37, (short) 18);
						else
							client.user.jump((short) 43, (short) 65);
						client.battle.enter();
						break;
				
				}
			}
		});
		
		client.on("enterbattle", new Event() {
			@Override
			public void execute(MAMClient client, Packet packet) {
				client.battle.doTurn();
			}
		});
		
		client.on("endturn", new Event() {
			@Override
			public void execute(MAMClient client, Packet packet) {
				client.battle.doTurn();
			}
		});
		
		client.on("exitbattle", new TestChecker(this.mover));
	}

}
