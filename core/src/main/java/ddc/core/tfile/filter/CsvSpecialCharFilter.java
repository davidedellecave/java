package ddc.core.tfile.filter;

import ddc.core.tfile.TFileException;

public class CsvSpecialCharFilter extends BaseTFileFilter {
	
	private String filter;
	public CsvSpecialCharFilter(char eol, char separator) {
		super();
		filter = "\b\t\f\n\r";
		filter = filter.replace(eol, '\0');
		filter = filter.replace(separator, '\0');		
	}
	
	@Override
	public char onChar(long lineNumber, long position, char ch) throws TFileException {
		if (filter.indexOf(ch)!=-1) return '\0';
		return ch;
	}

}
