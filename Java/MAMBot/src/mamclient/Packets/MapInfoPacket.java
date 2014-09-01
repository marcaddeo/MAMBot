package mamclient.Packets;

import mamclient.MAMClient;
import mamclient.GameObjects.GameMap;
import csocket.Packet;

public class MapInfoPacket implements IPacket {

	public String name;
	
	public int type;
	public int id;
	public int mapdoc;
	public int direction;
	
	public short x;
	public short y;
	
	public MapInfoPacket() {
	}
	
	public MapInfoPacket(Packet packet) {
		this.parse(packet);
	}
	
	public MapInfoPacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);
		
		client.user.x			= this.x;
		client.user.y 			= this.y;
		client.user.mapId		= this.id;
		
		client.gameMap.mapdoc	= this.mapdoc;
		client.gameMap.mapId	= this.id;
		
		client.gameMap.portals	= GameMap.portalList.get(this.mapdoc);
		
		Packet p = new Packet(20, 2057);
		p.putInt(10, 12);
		
		client.socket.sendPacket(p);
		
		client.emit("maploaded");
	}

	@Override
	public Packet pack() {
		return new Packet(0, 0);
	}

	@Override
	public void parse(Packet packet) {
		this.type 		= packet.getInt(0);
		this.id			= packet.getInt(8);
		this.mapdoc 	= packet.getInt(12);
		this.x			= packet.getShort(16);
		this.y 			= packet.getShort(18);
		this.direction	= packet.getInt(20);
		this.name		= packet.getString(28);
	}

}
