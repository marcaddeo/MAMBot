package mamclient.Packets;

import mamclient.MAMClient;
import mamclient.GameObjects.Enemy;
import csocket.Packet;

public class BattleDamagePacket implements IPacket {

	public boolean dead;
	
	public int id;
	public int target;
	public int damage;
	
	public BattleDamagePacket() {
	}
	
	public BattleDamagePacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	public BattleDamagePacket(Packet packet) {
		this.parse(packet);
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);
		
		if (this.dead) {
			for (int i = 0; i < client.battle.enemyList.size(); i++) {
				Enemy e = client.battle.enemyList.get(i);
				if (e.id == this.target)
					client.battle.enemyList.remove(e);
			}
		}
		
		// TODO make this actually update me and my pets HP
	}

	@Override
	public Packet pack() {
		return null;
	}

	@Override
	public void parse(Packet packet) {
		this.dead 	= (packet.getByte(3) == 101) ? true : false;
		this.id 	= packet.getInt(4);
		this.target = packet.getInt(8);
		this.damage	= packet.getInt(12);
	}

}
