import java.util.Map;

import csocket.Packet;
import mamclient.Event;
import mamclient.MAMClient;
import mamclient.GameObjects.Player;
import mamclient.Packets.MessagePacket;

public class CommandHandler implements Event {
	
	public MapMover mover;
	
	public CommandHandler(MapMover mover) {
		this.mover = mover;
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		MessagePacket msg = new MessagePacket(client, packet);
		
		if (client.debug) {
			if (msg.message.startsWith("@jump")) {
				String[] split 	= msg.message.split(" ", 2);
				split 			= split[1].replaceAll("\\s+", "").split(",", 2);
				short x 		= Short.parseShort(split[0]);
				short y			= Short.parseShort(split[1]);
				
				client.user.jump(x, y);
			} else if (msg.message.startsWith("@walk")) { 
				String[] split 	= msg.message.split(" ", 2);
				split 			= split[1].replaceAll("\\s+", "").split(",", 2);
				short x 		= Short.parseShort(split[0]);
				short y			= Short.parseShort(split[1]);
				
				client.user.walk(x, y);
			} else if (msg.message.startsWith("@login")) {
				final String[] split = msg.message.split(" ", 3);
				
				new Thread(new Runnable() {
	
					@Override
					public void run() {
						new MAMClient().login(split[1], split[2], "MythOfOrient");
					}
					
				}).start();
			} else if (msg.message.startsWith("@list")) {
				for (Map.Entry<Integer, Player> p : client.gameMap.playerList.entrySet()) {
					Player player = p.getValue();
					System.out.printf("%s (%d,%d)\n", player.name, player.x, player.y);
				}
			} else if (msg.message.startsWith("@battle")) {
				client.battle.enter();
			} else if (msg.message.startsWith("@move")) {
				String[] split = msg.message.split(" ", 2);
				this.mover.move(Integer.parseInt(split[1]));
			}
		}
	}

}
