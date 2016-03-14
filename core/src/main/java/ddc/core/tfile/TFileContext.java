package ddc.core.tfile;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import ddc.util.Chronometer;

public class TFileContext {
	private Path source = null;
	private char sourceEOL = '\n';
	private boolean appendNewLine = true;
	private String[] header;
	private TFileFilter filter;
	private Chronometer chron = null;
	private long lineTotal = 0;
	private long charTotal = 0;
	private long errorTotal = 0;
	private Map<String, String> params = new HashMap<String, String>();

	public void onError(TFileLineError error) throws TFileException {
		errorTotal++;
		filter.onError(error);
	}

	public void onError(long line, String source, Exception exception) throws TFileException {
		onError(new TFileLineError(line, source, exception));
	}

	//

	public Path getSource() {
		return source;
	}

	public void setSource(Path source) {
		this.source = source;
	}

	public char getSourceEOL() {
		return sourceEOL;
	}

	public void setSourceEOL(char sourceEOL) {
		this.sourceEOL = sourceEOL;
	}

	public boolean isAppendNewLine() {
		return appendNewLine;
	}

	public void setAppendNewLine(boolean appendNewLine) {
		this.appendNewLine = appendNewLine;
	}

	public TFileFilter getFilter() {
		return filter;
	}

	public void setFilter(TFileFilter filter) {
		this.filter = filter;
	}

//	public boolean hasFilter() {
//		return filter != null;
//	}

	public void setParam(String key, String value) {
		params.put(key, value);
	}

	public String getParam(String key) {
		return params.get(key);
	}

	public Chronometer getChron() {
		return chron;
	}

	public void setChron(Chronometer chron) {
		this.chron = chron;
	}

	public long getProcessed() {
		return lineTotal;
	}

	public void setLineTotal(long lineTotal) {
		this.lineTotal = lineTotal;
	}

	public long getSuccedeed() {
		return lineTotal-errorTotal;
	}
	
	public long getCharTotal() {
		return charTotal;
	}

	public void setCharTotal(long charTotal) {
		this.charTotal = charTotal;
	}

	public String[] getHeader() {
		return header;
	}

	public void setHeader(String[] header) {
		this.header = header;
	}

	public long getFailed() {
		return errorTotal;
	}

	public void setErrorTotal(long errorTotal) {
		this.errorTotal = errorTotal;
	}

	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append("File:[" + getSource() + "]");
		info.append(" Line #:[" + lineTotal + "]");
		info.append(" Char #:[" + charTotal + "]");
		info.append(" Error #:[" + errorTotal + "]");		
		if (chron != null)
			info.append(" Elapsed:[" + chron.getElapsed() + " ("+chron.toString()+")]" );
		return info.toString();
	}
}
