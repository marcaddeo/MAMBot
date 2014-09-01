package mamclient.GameObjects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;

public class GameMap {
	
	public static Map<Integer, List<Portal>> portalList;
	public static boolean loadedPortals = false;
	
	public Map<Integer, Player> playerList;
	
	public int mapId;
	public int mapdoc;
	
	public List<Portal> portals;
	
	public GameMap() {
		this.playerList = new HashMap<Integer, Player>();
		
		this.mapId 	= 0;
		this.mapdoc = 0;
		
		if (!GameMap.loadedPortals) this.loadPortals();
	}
	
	private void loadPortals() {
		try {
			GameMap.portalList 	= new HashMap<Integer, List<Portal>>();
			Ini mapdocList 		= new Ini(new File("/etc/MAMClient/ini/GameMap.ini"));
			
			for (Section section : mapdocList.values()) {
				try {
					List<Portal> pl 	= new ArrayList<Portal>();
					int mapdoc 			= Integer.parseInt(section.getName().replace("Map", ""));
					BufferedReader in 	= new BufferedReader(
							new FileReader("/etc/MAMClient/" + section.get("File")));
					
					String line;
					
					while ((line = in.readLine()) != null) {
						String[] split = line.split(",");
						
						int id 	= Integer.parseInt(split[0]);
						int x 	= Integer.parseInt(split[1]);
						int y 	= Integer.parseInt(split[2]);
						
						pl.add(new Portal(id, x, y));
					}
					
					in.close();
					
					GameMap.portalList.put(mapdoc, pl);
				} catch (FileNotFoundException e) {
					// Just ignore these errors, fucking thing
				}
			}
			
			GameMap.loadedPortals = true;
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addPlayer(Player player) {
		this.playerList.put(player.id, player);
	}
	
	public void delPlayer(Player player) {
		this.delPlayer(player.id);
	}
	
	public void delPlayer(String name) {
		for (Map.Entry<Integer, Player> p : this.playerList.entrySet()) {
			if (p.getValue().name.equals(name))
				this.playerList.remove(p.getKey());
		}
	}
	
	public void delPlayer(int id) {
		this.playerList.remove(id);
	}
	
	public Player findPlayer(String name) {
		for (Map.Entry<Integer, Player> p : this.playerList.entrySet()) {
			if (p.getValue().name.equals(name))
				return p.getValue();
		}
		return null;
	}
	
	public Player findPlayer(int id) {
		return this.playerList.get(id);
	}
}
