package mamclient.Packets;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import mamclient.MAMClient;
import csocket.Packet;

public class MessagePacket implements IPacket {
	
	public String sender;
	public String target;
	public String emotion;
	public String message;
	
	public int color;
	public int time; // don't really need this anymore

	public Channel 	channel;
	public Effect 	effect;
	
	public enum Channel {
		NONE((short) 0),
		UNKNOWN((short) 2000),
		PRIVATE((short) 2001),
		ACTION((short) 2002),
		TEAM((short) 2003),
		GUILD((short) 2004),
		SYSTEM((short) 2005),
		SPOUSE((short) 2006),
		NORMAL((short) 2007),
		SHOUT((short) 2008),
		FRIEND((short) 2009),
		BROADCAST((short) 2010),
		GM((short) 2011),
		VENDOR((short) 2104);
		
		private static final Map<Short, Channel> lookup = new HashMap<Short, Channel>();
		
		static {
			for (Channel c : EnumSet.allOf(Channel.class))
				lookup.put(c.getCode(), c);
		}
		
		public short code;
		
		private Channel(short code) {
			this.code = code;
		}
		
		private short getCode() { return code; }
		
		public static Channel get(short code) {
			return lookup.get(code);
		}
		
		@Override
		public String toString() {
			String s = super.toString();
			return s.substring(0, 1) + s.substring(1).toLowerCase();
		}
	}
	
	public enum Effect {
		NORMAL((short) 0),
		SCROLL((short) 1),
		GLOW((short) 2),
		SCROLL_GLOW((short) 3),
		BLAST((short) 8),
		SCROLL_BLAST((short) 9),
		GLOW_BLAST((short) 10),
		SCROLL_GLOW_BLAST((short) 11);
		
		private static final Map<Short, Effect> lookup = new HashMap<Short, Effect>();
		
		static {
			for (Effect e : EnumSet.allOf(Effect.class))
				lookup.put(e.getCode(), e);
		}
		
		public short code;
		
		private Effect(short code) {
			this.code = code;
		}
		
		private short getCode() { return code; }
		
		public static Effect get(short code) {
			return lookup.get(code);
		}
	}
	
	public MessagePacket() {
	}
	
	public MessagePacket(Packet packet) {
		this.parse(packet);
	}
	
	public MessagePacket(MAMClient client, Packet packet) {
		this.execute(client, packet);
	}
	
	public MessagePacket(String sender, String target, String message, Channel channel) {
		this(sender, target, message, channel, 0x0FFFFFF, Effect.NORMAL, " HackThePlanet ");
	}
	
	public MessagePacket(String sender, String target, String message, Channel channel, int color) {
		this(sender, target, message, channel, color, Effect.NORMAL, " HackThePlanet ");
	}
	
	public MessagePacket(String sender, String target, String message, Channel channel, int color, Effect effect) {
		this(sender, target, message, channel, color, effect, " HackThePlanet ");
	}
	
	public MessagePacket(String sender, String target, String message, Channel channel, int color, Effect effect, String emotion) {
		this.sender 	= sender;
		this.target 	= target;
		this.message 	= message;
		this.channel	= channel;
		this.color		= color;
		this.effect		= effect;
		this.emotion	= emotion;
		this.time		= 0;
	}
	
	public void print() {
		System.out.printf("[%s] ", this.channel.toString());
		if (this.channel == Channel.SYSTEM) 
			System.out.println(this.message);
		else if (this.channel == Channel.VENDOR)
			System.out.printf("(%s): %s\n", this.sender, this.message);
		else
			System.out.printf("%s speaks to %s: %s\n", this.sender, this.target, this.message);
	}
	
	@Override
	public void execute(MAMClient client, Packet packet) {
		this.parse(packet);
		client.emit("message", packet);
	}
	
	@Override
	public Packet pack() {
		Packet packet = new Packet();
		
		packet.putInt(this.color, 0);
		packet.putShort(this.channel.code, 4);
		packet.putShort(this.effect.code, 6);
		packet.putInt(this.time, 8);
		packet.putString(this.emotion, 16, 12);
		packet.putByte((byte) 3, 28);
		packet.putByte((byte) this.sender.length(), 29);
		packet.putString(this.sender, 30);
		packet.putByte((byte) this.target.length(), 30 + this.sender.length());
		packet.putString(this.target, 31 + this.sender.length());
		packet.putByte((byte) this.message.length(), 31 + this.sender.length() + this.target.length());
		packet.putString(this.message, 32 + this.sender.length() + this.target.length());
		
		packet.size = (short) (39 + this.sender.length() + this.target.length() + this.message.length());
		packet.id 	= (short) 1004;
		
		return packet;
	}

	@Override
	public void parse(Packet packet) {
		this.color 		= packet.getInt(0);
		this.channel	= Channel.get(packet.getShort(4));
		this.effect		= Effect.get(packet.getShort(6));
		this.time		= packet.getInt(8);
		this.emotion 	= packet.getString(12, 16);
		
		int sLength 	= (int) packet.getByte(29);
		int tLength		= (int) packet.getByte(30 + sLength);
		int mLength		= (int) packet.getByte(31 + sLength + tLength);
		
		this.sender 	= packet.getString(30, sLength);
		this.target		= packet.getString(31 + sLength, tLength);
		this.message 	= packet.getString(32 + sLength + tLength, mLength);
	}

}
