package ddc.core.tfile.filter;

import ddc.core.tfile.TFileException;

public class XslCsvFilter extends BaseTFileFilter {
	private String charToSkip;
	private boolean inField = false;
	private char oldSeparator;
	private char newSeparator;
	private char fieldCompound;

//	delimiter 	
//	CHAR
//	enclosed_by 	CHAR 	" 	"
//	escape 	CHAR 	" 	\
//	record_terminator 	CHAR 	\n or \r\n 	\n or \r\n
	public XslCsvFilter(char oldSeparator, char fieldCompound, char newSeparator, String charToSkip) {
		super();
		this.oldSeparator = oldSeparator;
		this.newSeparator = newSeparator;
		this.fieldCompound = fieldCompound;
		this.charToSkip = charToSkip + oldSeparator + newSeparator;
	}

	@Override
	public char onChar(long lineNumber, long position, char ch) throws TFileException {
		//if the line starts or ends with fieldCompound than skip fieldSeparator
		if (ch == fieldCompound) {
			inField = !inField;
			return '\0';
		}
		//if infield skip chars
		if (inField) {
			if (charToSkip.indexOf(ch) != -1)
				return '\0';
		} else {
			//if not in field replace separator
			if (ch == oldSeparator) {
				ch = newSeparator;
			}
		}
		return ch;
	}
}
