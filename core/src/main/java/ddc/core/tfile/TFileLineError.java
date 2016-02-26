package ddc.core.tfile;

public class TFileLineError {
	private long line=0;
	private String source=null;
	private Exception exception=null;
	//
	public TFileLineError(long line, String source, Exception exception) {
		super();
		this.line = line;
		this.source = source;
		this.exception = exception;
	}
	//	
	public long getLine() {
		return line;
	}
	public void setLine(long line) {
		this.line = line;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
}
