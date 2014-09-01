package mamclient;

public class MAMPassword {
	
	public static native byte[] encryptPassword(String password);
	
	static {
		System.loadLibrary("MAMPassword");
	}
	
}