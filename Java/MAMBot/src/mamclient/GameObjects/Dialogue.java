package mamclient.GameObjects;

import java.util.HashMap;
import java.util.Map;

public class Dialogue {

	public Map<Integer, String> optionList;
	
	public boolean open;
	
	public Dialogue() {
		this.optionList = new HashMap<Integer, String>();
		this.open		= false;
	}
	
}
