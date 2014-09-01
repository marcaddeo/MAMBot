package mamclient.Packets;

import java.util.ArrayList;
import java.util.List;

import mamclient.MAMClient;
import mamclient.GameObjects.Enemy;
import csocket.Packet;

public class EnemyInfoPacket implements IPacket {

	public int enemyCount;
	
	public List<Enemy> enemyList;
	
	public EnemyInfoPacket() {
		this.enemyCount = 0;
		this.enemyList 	= new ArrayList<Enemy>();
	}
	
	public EnemyInfoPacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	public EnemyInfoPacket(Packet packet) {
		this.parse(packet);
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);
		
		client.battle.enemyList 	= this.enemyList;
		client.battle.enemyCount	= this.enemyCount;
	}

	@Override
	public Packet pack() {
		return null;
	}

	@Override
	public void parse(Packet packet) {
		this.enemyCount = packet.getInt(0);
		
		for (int i = 0; i < this.enemyCount; i++) {
			Enemy enemy = new Enemy();
			enemy.id 	= packet.getInt(4 + (24 * i));
			enemy.look 	= packet.getShort(4 + (24 * i) + 4);
			enemy.level = packet.getShort(4 + (24 * i) + 6);
			enemy.name	= packet.getString(4 + (24 * i) + 8, 16);

			this.enemyList.add(enemy);
		}
	}

}
