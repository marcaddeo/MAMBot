package mamclient.GameObjects;

import java.util.ArrayList;
import java.util.List;

import mamclient.MAMClient;
import mamclient.Packets.BattleActionPacket;
import mamclient.Packets.BattleActionPacket.BattleAction;
import mamclient.Packets.EnterBattlePacket;
import mamclient.GameObjects.Fighter;
import mamclient.GameObjects.Enemy;

// TODO
// Currently this Battle class doesn't do very much. It's really only good for leveling/catching pets maybe
// I need to make it so it updates player/pet HP accordingly for damage and what-not

public class Battle {

	public MAMClient 		client;
	public List<Enemy> 		enemyList;
	public List<Fighter>	fighterList;
	
	public int enemyCount;
	public int turnCount;
	
	public boolean inBattle;
	
	public Battle(MAMClient client) {
		this.client 		= client;
		this.enemyList 		= new ArrayList<Enemy>();
		this.fighterList 	= new ArrayList<Fighter>();
	}
	
	public void enter() {
		this.client.socket.sendPacket(new EnterBattlePacket(this.client.user.characterId).pack());
	}
	
	public void reset() {
		this.enemyList.clear();
		this.fighterList.clear();
		this.enemyCount = 0;
		this.turnCount 	= 0;
		this.inBattle	= false;
	}
	
	public void run() {
		if (this.inBattle) {
			this.client.socket.sendPacket(
					new BattleActionPacket(
							BattleAction.HUMAN_RUN, (byte) this.turnCount,
							this.fighterList.get(0).id, 0).pack());
			this.client.socket.sendPacket(
					new BattleActionPacket(
							BattleAction.PET_RUN, (byte) this.turnCount,
							this.fighterList.get(1).id, 0).pack());
		}
	}
	
	public void doTurn() {
		if (this.inBattle) {
			for (Enemy e : this.enemyList) {
				this.client.socket.sendPacket(
						new BattleActionPacket(
								BattleAction.ATTACK, (byte) this.turnCount,
								this.fighterList.get(0).id, e.id).pack());
				this.client.socket.sendPacket(
						new BattleActionPacket(
								BattleAction.ATTACK, (byte) this.turnCount,
								this.fighterList.get(1).id, e.id).pack());
				this.turnCount++;
			}
		}
	}
	
}
