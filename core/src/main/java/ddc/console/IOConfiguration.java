package ddc.console;

public class IOConfiguration extends SourceFolderConfiguration {
	private String destination="destination";

	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public String toString() {
		return super.toString() +  " Destination:[" + getDestination() + "]";
	}

}
