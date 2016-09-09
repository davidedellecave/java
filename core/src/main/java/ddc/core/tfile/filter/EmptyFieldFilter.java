package ddc.core.tfile.filter;

import org.apache.commons.lang3.StringUtils;

import ddc.core.tfile.TFileException;

/**
 * Check if one of specified fields is empty (null or size=0) if it is true the line is not transformed
 * @author dellecave
 *
 */
public class EmptyFieldFilter extends BaseTFileFilter {
	private int[] requiredFields = new int[] { };
	private String separator = "\t";
	private boolean outCSV=false;
	
	
	public EmptyFieldFilter(int[] requiredFields, String separator, boolean outCSV) {
		super();
		this.requiredFields = requiredFields;
		this.separator = separator;
		this.outCSV=outCSV;
	}

	@Override
	public void onTransformLine(final long lineNumber, final StringBuilder sourceLine) throws TFileException {		
		String[] toks =  StringUtils.split(sourceLine.toString(), separator);
		boolean empty = false;
		for (int i=0; i<requiredFields.length; i++) {
			int pos = requiredFields[i];
			if (toks.length<=pos || toks[pos].length()==0) { 
				empty = true;
				if (outCSV)
					System.out.println(sourceLine + separator + "Field empty#:[" + pos +"]");
				else
					System.out.println("Field empty#:[" + pos + "] line #:[" + lineNumber + "] line:[" + sourceLine + "]");
			}
		}
		if (empty) {
			super.emptyLine(sourceLine);
		}
	}

}
