import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Node {
	public int x;
	public int y;
	public int id;
	
	public Node(int x, int y, int id) {
		this.x 	= x;
		this.y 	= y;
		this.id = id;
	}
}

class Step {
	public int type;
	public int param;
	
	public Step(int type, int param) {
		this.type 	= type;
		this.param 	= param;
	}
}

class Transport {
	public List<Step> steps;
	public int id;
	
	public Transport(int id) {
		this.steps 	= new ArrayList<Step>();
		this.id 	= id;
	}
}

public class GMap {
	
	public List<Node> 				nodes;
	public Map<Integer, Transport> 	transports;
	
	public int id;
	
	public GMap(int id) {
		this.nodes 		= new ArrayList<Node>();
		this.transports = new HashMap<Integer, Transport>();
		this.id 		= id;
	}
}
