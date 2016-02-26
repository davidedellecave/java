package ddc.core.tfile;

public interface TFileFilter {
	public StringBuilder onStartLine(long lineNumber, StringBuilder lineBuffer)  throws TFileException;
	public char onChar(long lineNumber, long position, char ch) throws TFileException;
	public StringBuilder onEndLine(long lineNumber, StringBuilder lineBuffer)  throws TFileException;
	public void onError(TFileLineError error)  throws TFileException;
}
