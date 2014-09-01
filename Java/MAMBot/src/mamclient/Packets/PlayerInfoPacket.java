package mamclient.Packets;

import mamclient.MAMClient;
import mamclient.GameObjects.Player;
import csocket.Packet;

public class PlayerInfoPacket implements IPacket {
	
	public String name;
	public String nickname;
	public String spouse;
	public String guild;
	public String guildPosition;
	
	public int id; //0
	
	public boolean pkEnable;
	
	public short x; //4
	public short y; //6
	public short direction; //11
	public short level; //16
	
	public byte look; //8
	public byte rank; //13
	public byte reborn; //14

	public PlayerInfoPacket() {
	}
	
	public PlayerInfoPacket(Packet packet) {
		this.parse(packet);
	}
	
	public PlayerInfoPacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);
		
		Player player 			= new Player();
		player.name 			= this.name; 		 
		player.nickname 		= this.nickname;		 
	 	player.spouse			= this.spouse;		 
		player.guild			= this.guild; 		 
	 	player.guildPosition	= this.guildPosition; 
		player.id 				= this.id;			 
		player.x				= this.x;			 
		player.y 				= this.y;			 
		player.look 			= this.look;			 
		player.direction 		= this.direction;	 
		player.rank 			= this.rank;			 
		player.reborn 			= this.reborn;		 
		player.level 			= this.level;		 
		player.pkEnable 		= this.pkEnable;
		
		client.gameMap.addPlayer(player);
		
		/*System.out.printf("name: %s; nickname: %s; spouse: %s; guild: %s; guildPosition: %s;\n"
				+ " id: %d; x: %d; y: %d; look: %d; direction: %d; rank: %d; reborn: %d; level: %d;\n"
				+ " pkEnable: %s;\n",
				this.name, this.nickname, this.spouse, this.guild, this.guildPosition,
				this.id, (int) this.x, (int) this.y, (int) this.look, (int) this.direction, (int) this.rank, (int) this.reborn, (int) this.level,
				(this.pkEnable) ? "true" : "false");*/
	}

	@Override
	public Packet pack() {
		return new Packet(0, 0);
	}

	@Override
	public void parse(Packet packet) {
		int nLength 		= packet.getByte(60);
		int mLength 		= packet.getByte(61 + nLength);
		int sLength 		= packet.getByte(62 + nLength + mLength);
		int gLength 		= packet.getByte(63 + nLength + mLength + sLength);
		int pLength 		= packet.getByte(65 + nLength + mLength + sLength + gLength);
		
		this.name 			= packet.getString(61, nLength);
		this.nickname		= packet.getString(62 + nLength, mLength);
	 	this.spouse			= packet.getString(63 + nLength + mLength, sLength);
		this.guild 			= packet.getString(64 + nLength + mLength + sLength, gLength);
	 	this.guildPosition	= packet.getString(66 + nLength + mLength + sLength + gLength, pLength);
		this.id				= packet.getInt(0);
		this.x				= packet.getShort(4);
		this.y				= packet.getShort(6);
		this.look			= (byte) packet.getByte(8);
		this.direction		= packet.getShort(11);
		this.rank			= (byte) packet.getByte(13);
		this.reborn			= (byte) packet.getByte(14);
		this.level			= packet.getShort(16);
		
		if (packet.getByte(20) > 0)
			this.pkEnable 	= true;
		else
			this.pkEnable 	= false;
	}

}
