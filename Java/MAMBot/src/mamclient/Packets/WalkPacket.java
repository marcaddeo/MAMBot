package mamclient.Packets;

import mamclient.MAMClient;
import mamclient.GameObjects.Player;
import csocket.Packet;

public class WalkPacket implements IPacket {

	public int playerId;
	
	public short startX;
	public short startY;
	public short destX;
	public short destY;
	
	public WalkPacket() {
	}
	
	public WalkPacket(Packet packet) {
		this.parse(packet);
	}
	
	public WalkPacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	public WalkPacket(int id, short sx, short sy, short dx, short dy) {
		this.playerId 	= id;
		this.startX 	= sx;
		this.startY 	= sy;
		this.destX 		= dx;
		this.destY 		= dy;
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);
		
		Player player 	= client.gameMap.findPlayer(this.playerId);
		player.x 		= this.destX;
		player.y		= this.destY;
	}

	@Override
	public Packet pack() {
		Packet packet = new Packet(16, 1005);
		
		packet.putInt(this.playerId, 0);
		packet.putShort(this.startX, 4);
		packet.putShort(this.startY, 6);
		packet.putShort(this.destX, 8);
		packet.putShort(this.destY, 10);
		
		return packet;
	}

	@Override
	public void parse(Packet packet) {
		this.playerId 	= packet.getInt(0);
		this.startX 	= packet.getShort(4);
		this.startY		= packet.getShort(6);
		this.destX		= packet.getShort(8);
		this.destY		= packet.getShort(10);
	}

}
