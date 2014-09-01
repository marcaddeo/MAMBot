package mamclient;

import mamclient.GameObjects.*;
import mamclient.Packets.*;
import csocket.*;

public class MAMClient extends EventHandler {
	public CSocket 		socket;
	public User			user;
	public GameMap 		gameMap;
	public Inventory 	inventory;
	public Battle		battle;
	public Dialogue		dialogue;
	
	public boolean 		debug;
	public boolean		connected;
	
	public MAMClient() {
		this.setClient();
		
		this.debug		= true;
		this.connected	= false;
		
		this.socket 	= new CSocket();
		this.user		= new User(this);
		this.gameMap 	= new GameMap();
		this.inventory	= new Inventory();
		this.battle		= new Battle(this);
		this.dialogue	= new Dialogue();
		
		this.addDefaultHandlers();
	}
	
	public void dump(Packet packet) {
		System.out.println("Size: " + packet.size + " ID: " + packet.id);
		for (int i = 0; i < packet.size - 4; i += 16) {
			System.out.printf("%06X: ", i);
			
			for (int n = 0; n < 16; n++) {
				if ((i + n) < packet.size - 4)
					System.out.printf("%02X ", (int) packet.data[i+n] & 0xFF);
				else
					System.out.printf("   ");
			}
			
			System.out.printf(" ");
			
			for (int n = 0; n < 16; n++) {
				if ((i + n) < packet.size - 4)
					System.out.printf("%c", 
							(packet.data[i + n] >= 32 && packet.data[i + n] < 127) ? packet.data[i + n] : '.');
			}
			
			System.out.println();
		}
	}
	
	public void login(String username, String password, String server) {
		Packet packet = new Packet(52, 1051);

		byte[] pass = MAMPassword.encryptPassword(password);
		
		packet.putString(username, 16, 0);
		
		for (int i = 0; i < pass.length; i++)
        	packet.putByte(pass[i], 16 + i);
		
		packet.putString(server, 16, 32);
		
		this.socket.connect("64.151.106.220", 9958);
		this.socket.sendPacket(packet);
		this.loop();
	}
	
	private void loop() {
		while (this.socket.isConnected())
			this.handle(this.socket.readPacket());
		client.emit("disconnect");
	}
	
	private void addDefaultHandlers() {
		this.addEvent("1002", new Event() {
			@Override
			public void execute(MAMClient client, Packet packet) {
				for (int i = 4; i < packet.data.length; i++)
					packet.putByte((byte) 0, i);
				
				packet.putByte((byte) 197, 8);
				
				client.socket.sendPacket(packet);
			}
		});
		this.addEvent("1004", new MessagePacket());
		this.addEvent("1005", new WalkPacket());
		this.addEvent("1006", new ActionPacket());
		this.addEvent("1007", new JumpPacket());
		this.addEvent("1008", new CharInfoPacket());
		this.addEvent("1016", new ItemInfoPacket());
		this.addEvent("1017", new ItemActionPacket());
		this.addEvent("1031", new MapInfoPacket());
		this.addEvent("1032", new PlayerInfoPacket());
		/** 
		 * First packet from the game server, don't really need any information in it though
		 * contains character name, look, and map id but we learn all this information later
		 * from the character info packet
		 */
		this.addEvent("1050", new Event() {
			@Override
			public void execute(MAMClient client, Packet packet) {
				client.user.name = packet.getString(16, 16);
				
				if (!client.connected) {
					client.connected = true;
					client.emit("connected");
				}
			}
		});
		// This connects us to the game server, don't really need a full class for it
		this.addEvent("1052", new Event() {
			@Override
			public void execute(MAMClient client, Packet packet) {
				client.user.accountId = packet.getInt(0);
				
				client.socket.generateKeys(packet.getInt(4));
				client.socket.connect(packet.getString(8), 9527);
				
				for (int i = 8; i < packet.data.length; i ++)
					packet.putByte((byte) 0, i);
				
				packet.putString("blacknull", 8);
				packet.size = 28;
				
				client.socket.resetCounters();
				
				client.socket.sendPacket(packet);
				
				client.socket.useNewKeys();
			}
		});
		this.addEvent("1059", new DummyEvent());
		
		this.addEvent("1201", new Event() {
			@Override
			public void execute(MAMClient client, Packet packet) {
				client.emit("loaded");
			}
		});
		
		this.addEvent("2000", new BattleActionPacket());
		this.addEvent("2001", new BattleDamagePacket());
		this.addEvent("2005", new EnterBattlePacket());
		this.addEvent("2007", new EnemyInfoPacket());
		this.addEvent("2008", new FighterInfoPacket());
		this.addEvent("2012", new EndTurnPacket());
		this.addEvent("2030", new NpcInfoPacket());
		this.addEvent("2031", new NpcUpdatePacket());
		this.addEvent("2033", new NpcDialoguePacket());
	}
}