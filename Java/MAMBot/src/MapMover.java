import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mamclient.Event;
import mamclient.MAMClient;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import csocket.Packet;

class Mover implements Event {

	public MapMover mover;
	
	public Mover(MapMover mover) {
		this.mover = mover;
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		if (mover.moving) mover.doNext();
	}
}

class Clicker implements Event {
	
	public MapMover mover;

	public Clicker(MapMover mover) {
		this.mover = mover;
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		if (mover.moving) {
			if (mover.sit.hasNext()) {
				Step step = mover.sit.next();
				
				if (step.type == 1)
					client.user.clickDialogue(step.param);
			}
		}
	}
}

public class MapMover {

	private static Map<Integer, GMap> mapList;
	private static DirectedGraph<Integer, DefaultWeightedEdge> graph;
	private static boolean loaded = false;
	
	private MAMClient client;
	
	public boolean moving;
	
	public List<GMap> 		path;
	public Iterator<GMap> 	it;
	public Transport		transport;
	public Iterator<Step>	sit;
	
	public MapMover(MAMClient client) {
		this.client = client;
		this.moving = false;
		
		if (!MapMover.loaded) this.loadMaps();
		
		this.client.on("maploaded", new Mover(this));
		this.client.on("dialogue", new Clicker(this));
	}
	
	public void move(int destination) {
		if (destination != this.client.user.mapId && !this.moving) {
			this.path 	= this.getPath(destination);
			this.it		= this.path.iterator();
			this.moving	= true;
			
			this.doNext();
		}
	}
	
	public void doNext() {
		if (this.it.hasNext()) {
			GMap map 	= MapMover.mapList.get(this.client.user.mapId);
			GMap next 	= it.next();
			
			for (Node node : map.nodes) {
				if (node.id == next.id) {
					if (node.x == -1) {
						this.transport 	= map.transports.get(node.y);
						this.sit 		= this.transport.steps.iterator();
						
						if (sit.hasNext()) {
							Step step = sit.next();

							if (step.type == 10)
								this.client.user.openNpc(step.param);
							
							break;
						}
					} else {
						this.client.user.jump((short) node.x, (short) node.y); 
						break;
					}
				}
			}
		} else 
			this.moving = false;
	}
	
	private void loadMaps() {
		MapMover.mapList 	= new HashMap<Integer, GMap>();
		MapMover.graph		=
				new DefaultDirectedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		try {
			Ini mapFile = new Ini(new File("/etc/MAMClient/ini/map.ini"));
			int mapNum 	= Integer.parseInt(mapFile.get("main", "MapNum")); 
			
			for (int i = 0; i < mapNum; i++) {
				Section section = mapFile.get("map" + i);
				
				if (section == null) continue;
				
				GMap map = new GMap(Integer.parseInt(section.get("Id")));
				
				for (int n = 0; n < Integer.parseInt(section.get("NodeNum")); n++) {
					String[] split = section.get("Node" + n).split(",");
					Node node = new Node(
							Integer.parseInt(split[0]),
							Integer.parseInt(split[1]),
							Integer.parseInt(split[2]));
					map.nodes.add(node);
					
					if (node.x == -1) {
						Section t = mapFile.get("transport" + node.y);
						
						if (t != null) {
							Transport transport = new Transport(node.y);
							
							for (int s = 0; s < Integer.parseInt(t.get("StepNum")); s++) {
								String[] sSplit = t.get("Step" + s).split(",");
								transport.steps.add(new Step(
										Integer.parseInt(sSplit[0]),
										Integer.parseInt(sSplit[1])));
							}
							
							map.transports.put(transport.id, transport);
						}
					}
				}
				
				mapList.put(map.id, map);
			}
		
			for (Map.Entry<Integer, GMap> e : mapList.entrySet())
				graph.addVertex(e.getKey());
			
			for (Map.Entry<Integer, GMap> e : mapList.entrySet()) {
				Iterator<Node> it = e.getValue().nodes.iterator();
				
				while (it.hasNext()) {
					Node node = it.next();
					try {
						graph.addEdge(e.getKey(), node.id);
					} catch (Exception ex) {
						// Ignore whatever
					}
				}
			}
			
			MapMover.loaded = true;
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private List<GMap> getPath(int destination) {
		List<GMap> path = new ArrayList<GMap>();
		
		DijkstraShortestPath<Integer, DefaultWeightedEdge> dsp = 
				new DijkstraShortestPath<Integer, DefaultWeightedEdge>(graph, 
						this.client.user.mapId, destination);

		for (DefaultWeightedEdge e : dsp.getPathEdgeList())
			path.add(MapMover.mapList.get(MapMover.graph.getEdgeTarget(e)));

		return path;
	}
	
}