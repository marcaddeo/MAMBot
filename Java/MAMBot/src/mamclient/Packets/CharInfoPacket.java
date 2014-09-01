package mamclient.Packets;

import mamclient.MAMClient;
import csocket.Packet;

public class CharInfoPacket implements IPacket {

	public String name;
	public String nickname;
	public String spouse;
	public String guildName;
	public String guildPosition;
	
	public char rank;
	public char reborn;
	
	public short look;
	public short level;
	public short currentHp;
	public short totalHp;
	public short currentMana;
	public short totalMana;
	public short availableStats;
	public short life;
	public short defense;
	public short attack;
	public short dexterity;
	public short mana;
	public short x;
	public short y;
	public short guildRank;
	
	public int characterId;
	public int money;
	public int reputation;
	public int cultivation;
	public int currentExp;
	public int wuxingPoints;
	public int kungfuPoints;
	public int petRaisingPoints;
	public int thieveryPoints;
	public int mapId;
	
	public CharInfoPacket() {
	}
	
	public CharInfoPacket(Packet packet) {
		this.parse(packet);
	}
	
	public CharInfoPacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);
		
		client.user.characterId 		= this.characterId; 	   
		client.user.look 				= this.look;			   
		client.user.level 				= this.level;		   
		client.user.currentHp 			= this.currentHp;	   
		client.user.totalHp				= this.totalHp;		   
		client.user.currentMana			= this.currentMana;	   
		client.user.totalMana			= this.totalMana;	   
		client.user.rank				= this.rank;			   
		client.user.reborn				= this.reborn; 		   
		client.user.money 				= this.money;		   
		client.user.reputation			= this.reputation;	   
		client.user.cultivation			= this.cultivation;	   
		client.user.currentExp			= this.currentExp;	   
		client.user.wuxingPoints		= this.wuxingPoints;	   
		client.user.kungfuPoints		= this.kungfuPoints;	   
		client.user.petRaisingPoints	= this.petRaisingPoints;
		client.user.thieveryPoints 		= this.thieveryPoints;  
		client.user.availableStats		= this.availableStats;  
		client.user.life				= this.life;			   
		client.user.defense				= this.defense;		   
		client.user.attack				= this.attack;		   
		client.user.dexterity			= this.dexterity;	   
		client.user.mana				= this.mana;			   
		client.user.x					= this.x;			   
		client.user.y					= this.y;			   
		client.user.mapId				= this.mapId;		   
		client.user.guildRank			= this.guildRank;	
		client.user.name				= this.name;			
		client.user.nickname			= this.nickname;		
		client.user.spouse				= this.spouse;		
		client.user.guildName 			= this.guildName;	
		client.user.guildPosition		= this.guildPosition;
		
		/*System.out.printf("name: %s; nickname: %s; spouse: %s; guild: %s; position: %s;\n",
				this.name, this.nickname, this.spouse, this.guildName, this.guildPosition);
		
		System.out.println("x: " + this.x + " y: " + this.y + " guil_rank: " + this.guildRank + " map: " + this.mapId);
		
		System.out.printf("ID: %d; level: %d; HP: %d/%d; Mana: %d/%d; rank: %d; reborn: %d" +
				" money: %d; repuation: %d; cultivation: %d; currentEXP: %d;" +
				" wuxing: %d; kungfu: %d; prp: %d; thievery; %d; stats: %d; life: %d;" +
				" dexterity: %d; mana: %d; map_id: %d; guild_rank: %d; x: %d; y: %d;\n",
				this.characterId, (int)this.level, this.currentHp, this.totalHp, this.currentMana,
				this.totalMana, (int) this.rank, (int) this.reborn, this.money, this.reputation, this.cultivation,
				this.currentExp, this.wuxingPoints, this.kungfuPoints, this.petRaisingPoints, this.thieveryPoints,
				this.availableStats, this.life, this.defense, this.attack, this.dexterity, this.mana,
				this.mapId, this.guildRank, this.x, this.y);*/
	}

	@Override
	public Packet pack() {
		return new Packet(0, 0);
	}

	@Override
	public void parse(Packet packet) {
		this.characterId 		= packet.getInt(0);
		this.look				= packet.getShort(4);
		this.level				= packet.getShort(6);
		this.currentHp			= packet.getShort(8);
		this.totalHp			= packet.getShort(10);
		this.currentMana		= packet.getShort(12);
		this.totalMana			= packet.getShort(14);
		this.rank				= packet.getByte(16);
		this.reborn 			= packet.getByte(17);
		this.money				= packet.getInt(20);
		this.reputation			= packet.getInt(24);
		this.cultivation		= packet.getInt(32);
		this.currentExp			= packet.getInt(36);
		this.wuxingPoints		= packet.getInt(40);
		this.kungfuPoints		= packet.getInt(44);
		this.petRaisingPoints 	= packet.getInt(48);
		this.thieveryPoints		= packet.getInt(52);
		this.availableStats		= packet.getShort(56);
		this.life				= packet.getShort(58);
		this.defense			= packet.getShort(60);
		this.attack				= packet.getShort(62);
		this.dexterity			= packet.getShort(64);
		this.mana				= packet.getShort(66);
		this.x					= packet.getShort(68);
		this.y					= packet.getShort(70);
		this.mapId				= packet.getInt(72);
		this.guildRank			= packet.getShort(84); // this might actually suppose to be 86

		int nLength 			= (int) packet.getByte(87);
		int mLength				= (int) packet.getByte(88 + nLength);
		int sLength				= (int) packet.getByte(89 + nLength + mLength);
		int gLength				= (int) packet.getByte(90 + nLength + mLength + sLength);
		int pLength				= (int) packet.getByte(92 + nLength + mLength + sLength + gLength);

		this.name				= packet.getString(88, nLength);
		this.nickname			= packet.getString(89 + nLength, mLength);
		this.spouse				= packet.getString(90 + nLength + mLength, sLength);
		this.guildName			= packet.getString(91 + nLength + mLength + sLength, gLength);
		this.guildPosition		= packet.getString(93 + nLength + mLength + sLength + gLength, pLength);
	}

}
