package mamclient.Packets;

import java.util.ArrayList;
import java.util.List;

import mamclient.MAMClient;
import mamclient.GameObjects.Fighter;
import csocket.Packet;

public class FighterInfoPacket implements IPacket {

	public List<Fighter> fighterList;
	
	public FighterInfoPacket() {
		this.fighterList = new ArrayList<Fighter>();
	}
	
	public FighterInfoPacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	public FighterInfoPacket(Packet packet) {
		this.parse(packet);
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);
		
		client.battle.fighterList 	= this.fighterList;
		client.battle.inBattle 		= true;
		client.emit("enterbattle", packet);
	}

	@Override
	public Packet pack() {
		return null;
	}

	@Override
	public void parse(Packet packet) {
		for (int i = 0; i < 2; i++) {
			Fighter fighter = new Fighter();
			fighter.id 		= packet.getInt(4 + (32 * i));
			fighter.look 	= packet.getShort(4 + (32 * i) + 4);
			fighter.level 	= packet.getShort(4 + (32 * i) + 6);
			fighter.name	= packet.getString(4 + (32 * i) + 8, 16);
			this.fighterList.add(fighter);
		}
	}

}
