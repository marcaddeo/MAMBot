package mamclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import csocket.Packet;

public class EventHandler {
	private Map<String, List<Event>> events;
	protected MAMClient client;
	
	public EventHandler() {
		this.events 	= new HashMap<String, List<Event>>();
	}

	public EventHandler(MAMClient client) {
		this.events 	= new HashMap<String, List<Event>>();
		this.client		= client;
	}
	
	public void setClient() {
		this.client = (MAMClient) this;
	}
	
	public void on(String command, final Event event) {
		this.addEvent(command, event);
	}
	
	protected void addEvent(String command, final Event event) {
		try {
			if (this.events.containsKey(command.toLowerCase())) {
				this.events.get(command.toLowerCase()).add(event);
			} else {
				this.events.put(command.toLowerCase(), new ArrayList<Event>() {
					private static final long serialVersionUID = -4306648421307951953L;
					{ add(event); }
				});
			}
		} catch (Exception e) {
			System.err.printf("EventHandler.addEvent Error: [%s]", command, e.getMessage());
		}
	}
	
	public void handle(Packet packet) {
		if (this.events.containsKey(Integer.toString(packet.id).toLowerCase())) {
			try {
				List<Event> events = this.events.get(Integer.toString(packet.id).toLowerCase());
				Iterator<Event> it = events.iterator();
				
				while (it.hasNext()) it.next().execute(this.client, packet);
			} catch (Exception e) {
				System.err.printf("EventHandler.handle Error: [%d] %s\n", packet.id, e.getMessage());
			}
		} else {
			if (this.client.debug)
				this.client.dump(packet);
		}
	}
	
	public void emit(String command) {
		if (this.events.containsKey(command.toLowerCase())) {
			try {
				List<Event> events = this.events.get(command.toLowerCase());
				Iterator<Event> it = events.iterator();
				
				while (it.hasNext()) it.next().execute(this.client, new Packet());
			} catch (Exception e) {
				System.err.printf("EventHandler.fireEvent Error: [%s] %s\n", command, e.getMessage());
			}
		}
	}
	
	public void emit(String command, Packet packet) {
		if (this.events.containsKey(command.toLowerCase())) {
			try {
				List<Event> events = this.events.get(command.toLowerCase());
				Iterator<Event> it = events.iterator();
				
				while (it.hasNext()) it.next().execute(this.client, packet);
			} catch (Exception e) {
				System.err.printf("EventHandler.fireEvent Error: [%s : %d] %s\n", command, packet.id, e.getMessage());
			}
		}
	}
}