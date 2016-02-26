package ddc.core.tfile.filter;

import ddc.core.tfile.TFileException;

public class SkipHeaderFilter extends BaseTFileFilter {

	@Override
	public StringBuilder onEndLine(long lineNumber, StringBuilder lineBuffer) throws TFileException {
		if (lineNumber==1) 
			return this.emptyLine(lineBuffer);
		return lineBuffer;
	}

}
