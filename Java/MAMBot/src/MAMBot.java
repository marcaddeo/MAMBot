import mamclient.MAMClient;

public class MAMBot {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MAMClient 	client 	= new MAMClient();
		MapMover 	mover	= new MapMover(client);
		
		client.debug 		= false;
		
		new MasterGodLeveler(client);
		
		client.login("user", "password", "MythOfOrient");
	}

}