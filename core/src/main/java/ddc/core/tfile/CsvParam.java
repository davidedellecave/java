package ddc.core.tfile;

public class CsvParam {
	private char delimiter=',';
	private char enclosing='"';
	private char escape='\\';
	
	public CsvParam(char delimiter, char enclosing, char escape) {
		super();
		this.delimiter = delimiter;
		this.enclosing = enclosing;
		this.escape = escape;
	}

	public char getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}

	public char getEnclosing() {
		return enclosing;
	}

	public void setEnclosing(char enclosing) {
		this.enclosing = enclosing;
	}

	public char getEscape() {
		return escape;
	}

	public void setEscape(char escape) {
		this.escape = escape;
	}



}
