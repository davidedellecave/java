package ddc.core.tfile.filter;

import ddc.core.tfile.TFileException;

public class SkipHeaderFilter extends BaseTFileFilter {

	@Override
	public void onTransformLine(final long lineNumber, final StringBuilder sourceLine) throws TFileException {		
		if (lineNumber==1) {
			super.emptyLine(sourceLine);
		}		
	}

}
