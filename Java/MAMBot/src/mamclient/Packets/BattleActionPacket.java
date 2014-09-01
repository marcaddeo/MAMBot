package mamclient.Packets;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import mamclient.MAMClient;
import csocket.Packet;

public class BattleActionPacket implements IPacket {

	public BattleAction action;
	
	public byte turnCount;
	
	public int id;
	public int target;
	public int weapon;
	
	public enum BattleAction {
		ATTACK((byte) 0),
		SPECIAL((byte) 1),
		DEFEND((byte) 2),
		PET_RUN((byte) 3),
		NO_ACTION((byte) 4),
		HUMAN_RUN((byte) 5),
		CATCH_PET((byte) 6),
		USE_ITEM((byte) 9);
		
		private static final Map<Byte, BattleAction> lookup = new HashMap<Byte, BattleAction>();
		
		static {
			for (BattleAction a : EnumSet.allOf(BattleAction.class))
				lookup.put(a.getCode(), a);
		}
		
		public byte code;
		
		private BattleAction(byte code) {
			this.code = code;
		}
		
		private byte getCode() { return code; }
		
		public static BattleAction get(byte code) {
			return lookup.get(code);
		}
		
		@Override
		public String toString() {
			String s = super.toString();
			return s.substring(0, 1) + s.substring(1).toLowerCase();
		}
	}
	
	public BattleActionPacket() {
	}
	
	public BattleActionPacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	public BattleActionPacket(Packet packet) {
		this.parse(packet);
	}
	
	public BattleActionPacket(BattleAction action, byte turnCount, int id, int target) {
		this(action, turnCount, id, target, 0);
	}
	
	public BattleActionPacket(BattleAction action, byte turnCount, int id, int target, int weapon) {
		this.action 	= action;
		this.turnCount 	= turnCount;
		this.id			= id;
		this.target		= target;
		this.weapon		= weapon;
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);
		// TODO add this to Battle
	}

	@Override
	public Packet pack() {
		Packet packet = new Packet(24, 2000);
		
		packet.putByte(this.action.code, 0);
		packet.putByte(this.turnCount, 1);
		packet.putInt(this.id, 4);
		packet.putInt(this.target, 8);
		packet.putInt(this.weapon, 12);
		
		return packet;
	}

	@Override
	public void parse(Packet packet) {
		this.action 	= BattleAction.get((byte) packet.getByte(0));
		this.turnCount 	= (byte) packet.getByte(1);
		this.id			= packet.getInt(4);
		this.target		= packet.getInt(8);
		this.weapon		= packet.getInt(12);
	}

}
