package ddc.core.tfile;

public interface TFileFilter {	
	public char onChar(long lineNumber, long position, char ch) throws TFileException;
	public void onTransformLine(final long lineNumber, final StringBuilder sourceLine) throws TFileException;
	public void onTransformLine(long lineNumber, StringBuilder sourceLine, String[] fields) throws TFileException;
	public void onError(TFileLineError error)  throws TFileException;
}
