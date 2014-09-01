package mamclient.Packets;

import mamclient.MAMClient;
import mamclient.GameObjects.Player;
import csocket.Packet;

public class JumpPacket implements IPacket {

	public int playerId;
	public int direction;
	public int mode;
	
	public short x;
	public short y;
	
	public JumpPacket() {
	}
	
	public JumpPacket(Packet packet) {
		this.parse(packet);
	}
	
	public JumpPacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	public JumpPacket(int playerId, short x, short y, int direction, int mode) {
		this.playerId 	= playerId;
		this.x 			= x;
		this.y			= y;
		this.direction	= direction;
		this.mode 		= mode;
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);
		
		if (this.mode == 2) {
			Player player 		= client.gameMap.findPlayer(this.playerId);
			player.x 			= this.x;
			player.y 			= this.y;
			player.direction 	= (short) this.direction;
		} else if (this.mode == 8) {
			client.gameMap.delPlayer(this.playerId);
		}
	}

	@Override
	public Packet pack() {
		Packet packet = new Packet(24, 1007);
		
		packet.putInt(this.playerId, 0);
		packet.putShort(this.x, 4);
		packet.putShort(this.y, 6);
		packet.putInt(this.direction, 8);
		packet.putInt(this.mode, 12);

		return packet;
	}

	@Override
	public void parse(Packet packet) {
		this.playerId 	= packet.getInt(0);
		this.x			= packet.getShort(4);
		this.y			= packet.getShort(6);
		this.direction	= packet.getInt(8);
		this.mode		= packet.getInt(12);
	}

}
