import mamclient.Event;
import mamclient.MAMClient;
import mamclient.GameObjects.Item;
import mamclient.GameObjects.Player;
import mamclient.Packets.ItemInfoPacket;
import mamclient.Packets.MessagePacket;
import mamclient.Packets.MessagePacket.Channel;
import csocket.Packet;

public class DrugHunter {

	public static long start 		= System.currentTimeMillis();
	public static int total 		= 0;
	
	public static boolean battle	= false;
	
	public static String owner 		= "jackyyll";
	public static String target 	= "";
	public static String pCode		= "";

	public static String [][] accounts = {
			{"test90", "abcd", "ArtOfWar005"},
			{"test84", "abcd", "ArtOfWar006"},
			{"test83", "abcd", "ArtOfWar007"},
			{"test81", "abcd", "ArtOfWar009"},
			{"test76", "abcd", "ArtOfWar010"},
			{"test75", "abcd", "ArtOfWar011"},
			{"test74", "abcd", "ArtOfWar012"},
			{"test73", "abcd", "dfgdfgd"},
			{"test71", "abcd", "ArtOfWar013"},
			{"test70", "abcd", "ArtOfWar014"}
	};
	
	public DrugHunter() {
		for (String[] acc : accounts) {
			try {
				System.out.printf("Launching %s...\n", acc[2]);
				launchClient(acc[0], acc[1]);
				Thread.sleep(5500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void launchClient(final String username, final String password) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MAMClient client = new MAMClient();
				
				client.debug = false;
				
				client.on("connected", new Event() {
					@Override
					public void execute(MAMClient client, Packet packet) {
						MessagePacket unlock = 
								new MessagePacket(client.user.name, 
										client.user.name,
										"/unlock " + DrugHunter.pCode, Channel.PRIVATE);
						client.socket.sendPacket(unlock.pack());
					}
				});
				
				client.on("1004", new Event() {
					@Override
					public void execute(MAMClient client, Packet packet) {
						MessagePacket msg = new MessagePacket(packet);

						if (msg.sender.equals(owner)) {
							if (msg.message.startsWith("@target")) {
								String[] split = msg.message.split(" ", 2);
								target = split[1];
							} else if (msg.message.startsWith("@start")) {
								battle = true;
								client.battle.enter();
							} else if (msg.message.startsWith("@stop")) {
								battle = false;
								client.battle.run();
							}
						}  
						
						if (msg.sender.equals(target)) {
							if (msg.message.startsWith("@drugs")) {
								Player target = client.gameMap.findPlayer(msg.sender);
								
								if (target != null) {
									for (Item item : client.inventory.itemList) {
										if (item.name.equals("HuntingDrug")) {
											client.user.giveItem(item, target);
										}
									}
								}
							}
						}
					}
				});
				
				client.on("loaded", new Event() {
					@Override
					public void execute(MAMClient client, Packet packet) {
						if (battle) client.battle.enter();
					}
				});
				
				client.on("receiveitem", new Event() {
					@Override
					public void execute(MAMClient client, Packet packet) {
						Item item = new Item(new ItemInfoPacket(packet));
						
						if (!item.equipped && !item.name.startsWith("#")) {
							for (String[] acc : accounts) {
								if (client.user.name.equals(acc[2])) {
									if (!item.name.equals("HuntingDrug")) {
										if (item.name.equals("Pain-easingDew"))
											client.user.useItem(item);
										else
											client.user.dropItem(item);
									} else if (item.name.equals("HuntingDrug")) {
										long elapsed = System.currentTimeMillis() - start;
										total++;
										System.out.printf("[%s] Got item: %s [%d/15] [%dT %f min]\n", 
												client.user.name, item.name, 
												client.inventory.itemCount,
												total, elapsed / (60 * 1000F));
									}
								}
							}
						}
					}
				});
				
				client.on("enterbattle", new Event() {
					@Override
					public void execute(MAMClient client, Packet packet) {
						client.battle.doTurn();
					}
				});
				
				client.on("endturn", new Event() {
					@Override
					public void execute(MAMClient client, Packet packet) {
						client.battle.doTurn();
					}
				});
				
				client.on("exitbattle", new Event() {
					@Override
					public void execute(MAMClient client, Packet packet) {
						if (battle) client.battle.enter();
					}
				});
				
				client.on("disconnect", new Event() {
					@Override
					public void execute(MAMClient client, Packet packet) {
						try {
							launchClient(username, password);
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				
				client.login(username, password, "MythOfOrient");
			}
			
		}).start();
	}
	
}
