package ddc.core.tfile.filter;

import ddc.core.tfile.TFileException;

public class SkipCharsFilter extends BaseTFileFilter {
	private String charToSkip;

	public SkipCharsFilter(String charToSkip) {
		super();
		this.charToSkip = charToSkip;

	}

	@Override
	public char onChar(long lineNumber, long position, char ch) throws TFileException {
		if (charToSkip.indexOf(ch) == -1) return ch;
		return '\0';
	}


}