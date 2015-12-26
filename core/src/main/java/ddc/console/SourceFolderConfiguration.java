package ddc.console;

/**
 * @author davidedc 2014
 *
 */
public class SourceFolderConfiguration extends Configuration {
	private String source="source";

	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String toString() {
		return "Source:[" + getSource() + "]";
	}
}
