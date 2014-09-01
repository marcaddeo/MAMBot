package mamclient.Packets;

import mamclient.MAMClient;
import csocket.Packet;

public class EnterBattlePacket implements IPacket {

	public int playerId;
	public int battleFormation;
	
	public byte action;
	public byte entityCount;
	
	public EnterBattlePacket() {
	}
	
	public EnterBattlePacket(Packet packet) {
		this.parse(packet);
	}
	
	public EnterBattlePacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	public EnterBattlePacket(int id) {
		this.playerId 			= id;
		this.action 			= 0;
		this.entityCount 		= 1;
		this.battleFormation 	= 0;
	}
	
	//TODO add a constructor for creating this packet to enter battle
	//TODO add an enum for battle actions, possibly make this global somewhere 
	//as most battle packets will use it
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);
		
		if (this.playerId == client.user.characterId && this.action == 3) {
			client.battle.reset();
			client.emit("exitbattle");
		}
	}

	@Override
	public Packet pack() {
		Packet packet = new Packet(14, 2005);
		
		packet.putByte(this.action, 0);
		packet.putByte(this.entityCount, 1);
		packet.putInt(this.playerId, 4);
		packet.putInt(this.battleFormation, 8);
		
		return packet;
	}

	@Override
	public void parse(Packet packet) {
		this.action 			= (byte) packet.getByte(0);
		this.entityCount 		= (byte) packet.getByte(1);
		this.playerId			= packet.getInt(4);
		this.battleFormation	= packet.getInt(8);
	}

}
