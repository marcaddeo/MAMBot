package mamclient.Packets;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import mamclient.MAMClient;
import csocket.Packet;

public class ItemActionPacket implements IPacket {

	public int 			itemId;
	public ItemAction 	action;
	
	public enum ItemAction {
		USE(1),
		DROP(3),
		REMOVE(4);
		
		private static final Map<Integer, ItemAction> lookup = new HashMap<Integer, ItemAction>();
		
		static {
			for (ItemAction a : EnumSet.allOf(ItemAction.class))
				lookup.put(a.getCode(), a);
		}
		
		public int code;
		
		private ItemAction(int code) {
			this.code = code;
		}
		
		private int getCode() { return code; }
		
		public static ItemAction get(int code) {
			return lookup.get(code);
		}
		
		@Override
		public String toString() {
			String s = super.toString();
			return s.substring(0, 1) + s.substring(1).toLowerCase();
		}
	}
	
	public ItemActionPacket() {
	}
	
	public ItemActionPacket(Packet packet) {
		this.parse(packet);
	}
	
	public ItemActionPacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	public ItemActionPacket(int id, ItemAction action) {
		this.itemId = id;
		this.action = action;
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);

		switch (this.action) {
			case USE:
				client.inventory.findItem(this.itemId).equipped = true;
				break;
				
			case DROP:
			case REMOVE:
				client.inventory.delItem(this.itemId);
				break;
		}
	}

	@Override
	public Packet pack() {
		Packet packet = new Packet(12, 1017);
		
		packet.putInt(this.itemId, 0);
		packet.putInt(this.action.code, 4);
		
		return packet;
	}

	@Override
	public void parse(Packet packet) {
		this.itemId = packet.getInt(0);
		this.action = ItemAction.get(packet.getInt(4));
	}

}
