package ddc.core.tfile.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ddc.core.json.JSONUtil;
import ddc.core.tfile.TFileException;

/**
 * Transform csv line to json line
 * TFileContext header property must be set (see CsvHeaderFilter)
 * @author dellecave
 *
 */
public class CsvJsonFilter extends BaseTFileFilter {
	private final static Logger logger = Logger.getLogger(CsvJsonFilter.class);
	private String separator = String.valueOf(BaseTFileFilter.TAB);
	private boolean ignoreExcedingFields = true;
	private StringBuilder targetLine = new StringBuilder(); 

	public CsvJsonFilter(char separator, boolean ignoreExcedingFields) {
		super();
		this.separator = String.valueOf(separator);
		this.ignoreExcedingFields=ignoreExcedingFields;
	}

	private boolean repeatErrorDebug = true;
	@Override	
	public void onTransformLine(final long lineNumber, final StringBuilder sourceLine) throws TFileException {
		emptyLine(targetLine);
		String[] header = getContext().getHeader();
		String line = sourceLine.toString();
		String[] toks = StringUtils.splitPreserveAllTokens(line, separator);
		
		if (header.length!=toks.length) {		
			if (toks.length>header.length && ignoreExcedingFields) {
				logger.debug("The number of fields is greater than fields declared on header - ignored");
			} else {
				String info = "Error on number of fields - headerFields#:[" + header.length + "] lineFields#:[" + toks.length + "]";
				if (repeatErrorDebug) {
					logger.debug(info);
				}
				repeatErrorDebug=false;
				Exception e =  new TFileException(info);
				getContext().onError(lineNumber, sourceLine.toString(), e);
				return;
			}
		}		
		targetLine.append("{");
		for (int i = 0; i < header.length; i++) {
			targetLine.append("\"").append(header[i]).append("\"");
			targetLine.append(":");
			if (toks.length > i) {
				targetLine.append(JSONUtil.quote(toks[i]));
			} else {
				targetLine.append("\"").append("\"");
			}
			if (i < header.length - 1)
				targetLine.append(",");
		}
		targetLine.append("}");
		copyLine(targetLine, sourceLine);
	}

}
