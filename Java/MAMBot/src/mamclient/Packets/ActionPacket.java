package mamclient.Packets;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import mamclient.MAMClient;
import csocket.Packet;

public class ActionPacket implements IPacket {

	public int playerId;
	
	public short x;
	public short y;
	
	public Action actionId;
	
	public enum Action {
		FAINT(4),
		WAVE(6),
		KNEEL(7),
		CRY(8),
		ANGRY(9),
		SIT(10),
		HAPPY(12),
		BOW(13),
		THROW(15);
		
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
	
	public ActionPacket() {
	}
	
	public ActionPacket(Packet packet) {
		this.parse(packet);
	}
	
	public ActionPacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	public ActionPacket(int id, short x, short y, Action action) {
		this.playerId	= id;
		this.x			= x;
		this.y			= y;
		this.actionId	= action;
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);
	}

	@Override
	public Packet pack() {
		Packet packet = new Packet(16, 1006);
		
		packet.putInt(this.playerId, 0);
		packet.putShort(this.x, 4);
		packet.putShort(this.y, 6);
		packet.putInt(this.actionId.code, 8);
		
		return packet;
	}

	@Override
	public void parse(Packet packet) {
		this.playerId 	= packet.getInt(0);
		this.x			= packet.getShort(4);
		this.y 			= packet.getShort(6);
		this.actionId	= Action.get(packet.getInt(8));
	}

}
