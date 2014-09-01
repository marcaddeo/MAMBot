package csocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import crypto.Crypto;

public class CSocket extends Crypto {
	
	public Socket 				socket;
	private DataOutputStream	outStream;
	private DataInputStream		inStream;
	private boolean 			connected;
	
	public CSocket() {
		this.socket 	= new Socket();
		this.connected 	= false;
	}
	
	public void connect(String address, int port) {
		try {
			if (this.socket.isConnected())
				this.socket.close();
			
			this.connected 		= false;
			this.socket 		= new Socket(address, port);
			OutputStream 	_os = this.socket.getOutputStream();
			InputStream 	_in = this.socket.getInputStream();
			
			this.outStream 	= new DataOutputStream(_os);
			this.inStream	= new DataInputStream(_in);
			this.connected	= true;
		} catch (IOException e) {
			System.err.println("CSocket.connect Error: " + e.getMessage());
		}
	}
	
	public boolean isConnected() {
		return this.connected;
	}
	
	public Packet readPacket() {
		Packet packet;
		byte[] header = new byte[4];
		byte[] buffer;
		
		try {
			int status = this.inStream.read(header, 0, 4);
			
			if (status == 0) {
				System.err.println("CSocket.readPacket Error: Emtpy packet...");
				this.connected = false;
				return new Packet(0, 0);
			}
			
			if (status == -1) 
				System.err.println("CSocket.readPacket Error: End of stream reached...");
			
			header = this.incoming(header);
			
			int size 	= ((header[1] << 8) | (header[0] & 0xFF));
			int id 		= ((header[3] << 8) | (header[2] & 0xFF));
			
			if (size <= 0 || size > 4096) {
				this.connected = false;
				return new Packet(0, 0);
			}
			
			packet = new Packet(size, id);
			
			buffer = new byte[packet.size - 4];
			
			status = this.inStream.read(buffer, 0, packet.size - 4);
			
			packet.setData(this.incoming(buffer));

			return packet;
			
		} catch (IOException e) {
			System.err.println("CSocket.readPacket Error: " + e.getMessage());
		}
		
		this.connected = false;
		return new Packet(0, 0);
	}
	
	public void sendPacket(Packet packet) {
		byte[] buffer = new byte[packet.size];

		byte sizelow 	= (byte) (packet.size >>> 8);
		byte sizehigh 	= (byte) (packet.size & 0xFF);
		byte idlow 		= (byte) (packet.id >>> 8);
		byte idhigh 	= (byte) (packet.id & 0xFF);

		buffer[0] = sizehigh;
		buffer[1] = sizelow;
		buffer[2] = idhigh;
		buffer[3] = idlow;
		
		for (int i = 0; i < packet.size - 4; i++) 
			buffer[4 + i] = (byte) packet.data[i];

		try {
			this.outStream.write(this.outgoing(buffer), 0, packet.size);
		} catch (IOException e) {
			System.err.println("CSocket.sendPacket Error: " + e.getMessage());
		}
	}
	
}