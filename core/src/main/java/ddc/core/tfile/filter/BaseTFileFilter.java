package ddc.core.tfile.filter;

import ddc.core.tfile.TFileContext;
import ddc.core.tfile.TFileContextListener;
import ddc.core.tfile.TFileException;
import ddc.core.tfile.TFileFilter;
import ddc.core.tfile.TFileLineError;

public class BaseTFileFilter implements TFileFilter, TFileContextListener {
	private TFileContext context = null;
	
	public static final char EMPTY_CHAR='\0';
	
	public static final char CARRIAGE_RETURN='\r';
	public static final String CARRIAGE_RETURN_XML="&#13;";
	
	public static final char LINE_FEED='\n';
	public static final String LINE_FEED_XML="&#10;";
	
	public static final char TAB='\t';
	public static final String TAB_XML="&#9;";
	
	public static final char COMMA=',';
	public static final String COMMA_XML="&#44;";

	public static final char BACKSLASH='\\';
	public static final String BACKSLASH_XML="&#92;"; 

	public static final char DOUBLEQUOTE='"';
	public static final String DOUBLEQUOTE_XML="&#34;";

	public static final char QUOTE='\'';
	public static final String QUOTE_XML="&#39;";
	
	public static final String EMPTY_CHAR_S="";
	public static final String CARRIAGE_RETURN_S="\r";
	public static final String LINE_FEED_S="\n";
	public static final String TAB_S="\t";

	public void emptyLine(StringBuilder lineBuffer) {
		lineBuffer.delete(0, lineBuffer.length());
	}
	
	public void copyLine(StringBuilder source, StringBuilder target) {
		emptyLine(target);
		target.append(source);
	}

	public void copyLine(String source, StringBuilder target) {
		emptyLine(target);
		target.append(source);
	}

	public boolean isEOL(char ch) {
		return (ch==CARRIAGE_RETURN || ch==LINE_FEED);
	}
	
	
	@Override
	public char onChar(long lineNumber, long position, char ch) throws TFileException {
		return ch;
	}

	
	@Override
	public void onTransformLine(final long lineNumber, final StringBuilder sourceLine) throws TFileException {	
	}
	
	@Override
	public void onTransformLine(long lineNumber, StringBuilder lineBuffer, String[] fields) throws TFileException {
	}
	
	@Override
	public void onOpen(TFileContext context) throws TFileException {
		this.context = context;		
	}


	@Override
	public void onClose(TFileContext context) throws TFileException {
		this.context = context;	
	}

	
	public TFileContext getContext() {
		return context;
	}


	@Override
	public void onError(TFileLineError error) throws TFileException {
	}


}
