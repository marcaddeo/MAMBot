package mamclient.Packets;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import mamclient.MAMClient;
import csocket.Packet;

public class NpcUpdatePacket implements IPacket {

	public int 		id;
	public Action 	action;
	
	public enum Action {
		REMOVE(3);
		
		private static final Map<Integer, Action> lookup = new HashMap<Integer, Action>();
		
		static {
			for (Action a : EnumSet.allOf(Action.class))
				lookup.put(a.getCode(), a);
		}
		
		public int code;
		
		private Action(int code) {
			this.code = code;
		}
		
		private int getCode() { return code; }
		
		public static Action get(int code) {
			return lookup.get(code);
		}
		
		@Override
		public String toString() {
			String s = super.toString();
			return s.substring(0, 1) + s.substring(1).toLowerCase();
		}
	}
	
	public NpcUpdatePacket() {
	}
	
	public NpcUpdatePacket(Packet packet) {
		this.parse(packet);
	}
	
	public NpcUpdatePacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);
		// TODO Make this update the NpcList that will be contained in MAMClient
	}

	@Override
	public Packet pack() {
		return new Packet(0, 0);
	}

	@Override
	public void parse(Packet packet) {
		this.id		= packet.getInt(0);
		this.action	= Action.get(packet.getInt(4));
	}

}
