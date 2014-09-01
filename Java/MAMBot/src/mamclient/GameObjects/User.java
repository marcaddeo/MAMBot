package mamclient.GameObjects;

import java.util.Iterator;

import mamclient.MAMClient;
import mamclient.Packets.GiveItemPacket;
import mamclient.Packets.ItemActionPacket;
import mamclient.Packets.ItemActionPacket.ItemAction;
import mamclient.Packets.JumpPacket;
import mamclient.Packets.NpcClickDialoguePacket;
import mamclient.Packets.OpenNpcPacket;
import mamclient.Packets.WalkPacket;

public class User {

	private MAMClient client;
	
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
	public int accountId;
	public int money;
	public int reputation;
	public int cultivation;
	public int currentExp;
	public int wuxingPoints;
	public int kungfuPoints;
	public int petRaisingPoints;
	public int thieveryPoints;
	public int mapId;
	
	public boolean inBattle;
	
	public User(MAMClient client) {
		this.client 	= client;
		this.inBattle 	= false;
	}
	
	public void walk(short x, short y) {
		this.walk(this.x, this.y, x, y);
	}
	
	public void walk(short sx, short sy, short dx, short dy) {
		this.x = dx;
		this.y = dy;
		this.client.socket.sendPacket(new WalkPacket(this.characterId, sx, sy, dx, dy).pack());
	}
	
	public void jump(short x, short y) {
		this.jump(x, y, 0, 2);
	}
	
	public void jump(short x, short y, int dir, int mode) {
		this.x = x;
		this.y = y;
		
		this.client.socket.sendPacket(new JumpPacket(this.characterId, x, y, dir, mode).pack());
		
		Iterator<Portal> it = client.gameMap.portals.iterator();
		
		while (it.hasNext()) {
			Portal portal = it.next();
			
			if (portal.x == x && portal.y == y) 
				this.jump((short) 0, (short) 0, portal.id, 8);
		}
	}
	
	public void useItem(Item item) {
		this.client.socket.sendPacket(new ItemActionPacket(item.id, ItemAction.USE).pack());
	}
	
	public void useItem(int id) {
		this.useItem(this.client.inventory.findItem(id));
	}
	
	public void useItem(String name) {
		this.useItem(this.client.inventory.findItem(name));
	}
	
	public void dropItem(Item item) {
		this.client.socket.sendPacket(new ItemActionPacket(item.id, ItemAction.REMOVE).pack());
	}
	
	public void dropItem(int id) {
		this.dropItem(this.client.inventory.findItem(id));
	}
	
	public void dropItem(String name) {
		this.dropItem(this.client.inventory.findItem(name));
	}
	
	public void giveItem(Item item, Player target) {
		GiveItemPacket give = new GiveItemPacket(this.characterId, target.id, item.id, 2);
		this.client.socket.sendPacket(give.pack());
	}
	
	public void openNpc(int id) {
		this.client.socket.sendPacket(new OpenNpcPacket(id).pack());
	}
	
	public void clickDialogue(int option) {
		this.client.socket.sendPacket(new NpcClickDialoguePacket(option).pack());
	}
	
}
