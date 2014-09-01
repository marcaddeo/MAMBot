package mamclient.Packets;

import java.util.HashMap;
import java.util.Map;

import mamclient.MAMClient;
import csocket.Packet;

public class NpcDialoguePacket implements IPacket {

	public Map<Integer, String> optionList;
	
	public String text;
	
	public int optionCount;
	
	public NpcDialoguePacket() {
		this.optionList = new HashMap<Integer, String>(); 
	}
	
	public NpcDialoguePacket(Packet packet) {
		this.parse(packet);
	}
	
	public NpcDialoguePacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);
		client.dialogue.optionList 	= this.optionList;
		client.dialogue.open		= true;
		
		client.emit("dialogue");
	}

	@Override
	public Packet pack() {
		return new Packet(0, 0);
	}

	@Override
	public void parse(Packet packet) {
		int nStrings 	= packet.getByte(8);
		int index		= 10;
		boolean is_opt	= true;
		
		for (int i = 0; i < nStrings; i++) {
			int length 		= packet.getByte(index - 1);
			String buffer 	= packet.getString(index, length);
			
			index += length + 1;
			
			is_opt = (length == 0) ? false : is_opt;
			
			if (is_opt) {
				this.optionList.put(i + 1, buffer);
				this.optionCount++;
			} else {
				this.text = buffer;
			}
		}
	}

}
