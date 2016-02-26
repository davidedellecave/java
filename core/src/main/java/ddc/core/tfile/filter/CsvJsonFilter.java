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
//	private String filter = null;

	public CsvJsonFilter(char separator, boolean ignoreExcedingFields) {
		super();
		this.separator = String.valueOf(separator);
		this.ignoreExcedingFields=ignoreExcedingFields;
	}

	private boolean repeatErrorDebug = true;
	@Override
	public StringBuilder onEndLine(long lineNumber, StringBuilder lineBuffer) throws TFileException {
		String[] header = getContext().getHeader();
		StringBuilder json = new StringBuilder(256);
		String line = lineBuffer.toString();
		String[] toks = StringUtils.splitPreserveAllTokens(line, separator);
		
		if (header.length!=toks.length) {		
			if (toks.length>header.length && ignoreExcedingFields) {
				logger.debug("Fields are greater than header - ignored");
			} else {
				String info = "Error on number of fields - headerFields#:[" + header.length + "] lineFields#:[" + toks.length + "]";
				if (repeatErrorDebug) {
					logger.debug(info);
				}
				repeatErrorDebug=false;
				Exception e =  new TFileException(info);
				getContext().onError(lineNumber, lineBuffer.toString(), e);
				return super.emptyLine(lineBuffer);
			}
		}
		
		json.append("{");
		for (int i = 0; i < header.length; i++) {
			json.append("\"").append(header[i]).append("\"");
			json.append(":");
			if (toks.length > i) {
				json.append(JSONUtil.quote(toks[i]));
			} else {
				json.append("\"").append("\"");
			}
			if (i < header.length - 1)
				json.append(",");
		}
		json.append("}");
		return json;
	}

}
