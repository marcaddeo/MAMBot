package crypto;

public class Counter {

	public int first;
	public int second;
	
	public Counter() {
		this.first 	= 0;
		this.second = 0;
	}
	
	public void reset() {
		this.first 	= 0;
		this.second = 0;
	}
	
	public void increment() {
		this.first++;
		
		if (this.first == 256) {
			this.first = 0;
			this.second++;
		}
		
		if (this.second == 256) {
			this.second = 0;
		}
	}
	
}