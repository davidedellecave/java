package ddc.event;

public class EventData {
	private String id = null;
	private byte[] data = null;
	
	public EventData(String id, byte[] data) {
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
