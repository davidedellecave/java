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
	
	public static final String EMPTY_CHAR_S="";
	public static final String CARRIAGE_RETURN_S="\r";
	public static final String LINE_FEED_S="\n";
	public static final String TAB_S="\t";

	public StringBuilder emptyLine(StringBuilder lineBuffer) {
		lineBuffer.delete(0, lineBuffer.length());
		return lineBuffer;
	}
	
	
	@Override
	public StringBuilder onStartLine(long lineNumber, StringBuilder lineBuffer) throws TFileException {
		return lineBuffer;
	}

	
	@Override
	public char onChar(long lineNumber, long position, char ch) throws TFileException {
		return ch;
	}

	
	@Override
	public StringBuilder onEndLine(long lineNumber, StringBuilder lineBuffer) throws TFileException {
		consumeLine(lineNumber, lineBuffer);
		return lineBuffer;
	}
	
	public void consumeLine(long lineNumber, StringBuilder lineBuffer) throws TFileException {
	}


	@Override
	public void onOpen(TFileContext context) throws TFileException {
		this.context = context;		
	}


	@Override
	public void onClose(TFileContext context) throws TFileException {
		
	}

	
	public TFileContext getContext() {
		return context;
	}


	@Override
	public void onError(TFileLineError error) throws TFileException {
	}
}
