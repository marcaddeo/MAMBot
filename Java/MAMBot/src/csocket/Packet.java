package csocket;

public class Packet {

	public short 	size;
	public short 	id;
	public char[] 	data;
	
	public Packet() {
		this.id 	= 0;
		this.size 	= 0;
		this.data 	= new char[2048];
	}
	
	public Packet(int size, int id) {
		this((short) size, (short) id);
	}
	
	public Packet(short size, short id) {
		this.size 	= size;
		this.id		= id;
		this.data 	= new char[size];
	}
	
	public void setData(byte[] data) {
		this.data = new char[data.length];
		
		for (int i = 0; i < this.data.length; i++)
			this.data[i] = (char) data[i];
	}
	
	public void setData(char[] data) {
		this.data = new char[data.length];
		
		for (int i = 0; i < this.data.length; i++)
			this.data[i] = data[i];
	}
	
	public void putInt(int num, int index) {
		for (int i = 0; i < 4; ++i) {
			this.data[index + i] = (char) (num & 0xFF);
			num >>= 8;
		}
	}
	
	public void putShort(short num, int index) {
		char low 	= (char) (num >>> 8);
		char high 	= (char) (num & 0xFF);
		
		this.data[index] 	= high;
		this.data[index+1] 	= low; 
	}
	
	public void putByte(byte b, int index) {
		this.data[index] = (char) b;
	}
	
	public void putByte(char b, int index) {
		this.data[index] = b;
	}
	
	public void putString(String string, int index) {
		for (int i = 0; i < string.length(); i++)
			this.data[index + i] = string.charAt(i);
	}
	
	public void putString(String string, int length, int index) {
		for (int i = 0; i < length; i++) {
			if (i == string.length())
				break;
			else
				this.data[index + i] = string.charAt(i);
		}
	}
	
	public String getString(int index) {
		String str = "";
		int i = 0;
		
		while (this.data[index + i] != 0) {
			str = str + this.data[index + i];
			i++;
		}
		
		return str;
	}
	
	public String getString(int index, int length) {
		String str = "";
		
		for (int i = 0; i < length && this.data[index + i] != 0; i++)
			str = str + this.data[index + i];
		
		return str;
	}
	
	public int getInt(int index) {
		return (this.data[index + 3] << 24)
                + ((this.data[index + 2] & 0xFF) << 16)
                + ((this.data[index + 1] & 0xFF) << 8)
                + (this.data[index] & 0xFF);
	}
	
	public short getShort(int index) {
		return (short) ((this.data[index + 1] << 8) | (this.data[index] & 0xFF));
	}
	
	public char getByte(int index) {
		return this.data[index];
	}
	
	public byte getByteR(int index) {
		return (byte) this.data[index];
	}
	
}