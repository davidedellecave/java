package ddc.core.tfile.filter;

import ddc.core.tfile.TFileException;

/**
 * Update the context source line fields
 * To have the original source line this filter must put as first filter
 * @author dellecave
 */
public class SourceLineFilter extends BaseTFileFilter {
	private StringBuilder source = new StringBuilder();
	
	@Override
	public StringBuilder onStartLine(long lineNumber, StringBuilder lineBuffer) throws TFileException {
		source = super.emptyLine(source);
		return lineBuffer;
	}

	@Override
	public char onChar(long lineNumber, long position, char ch) throws TFileException {
		if (ch!='\0') source.append(ch);
		return ch;
	}

	public StringBuilder getSource() {
		return source;
	}
	
	
}