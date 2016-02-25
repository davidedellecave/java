package ddc.event;

public class DataEvent {
	private String id = null;
	private byte[] data = null;
	
	public DataEvent(String id, byte[] data) {
		this.id=id;
		this.data=data;
	}
	
	public String getId() {
		return id;
	}

	public byte[] getData() {
		return data;
	}
	
}
