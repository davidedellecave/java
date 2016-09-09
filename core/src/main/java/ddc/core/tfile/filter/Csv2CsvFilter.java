package ddc.core.tfile.filter;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ddc.core.tfile.CsvParam;
import ddc.core.tfile.TFileException;
import ddc.csv.CsvWriter;

public class Csv2CsvFilter extends BaseTFileFilter {
	private final static Logger logger = Logger.getLogger(Csv2CsvFilter.class);
	// private CsvParam sourceParam = null;
	private CsvParam targetParam = null;
	// private String targetMetaChar = null;
	// private StringBuilder targetLine = new StringBuilder();
	// private boolean sourceHasEscapeUnquotedValues = false;
	// private boolean targetEscapeUnquotedValues = true;
	// private boolean targetReplaceEmptyValue = false;
	// private String targetEmptyValue = "null";
	// private int fieldCounter = 0;
	// private int[] fieldPos = new int[128];

	public Csv2CsvFilter(CsvParam targetParam) {
		this.targetParam = targetParam;
	}

	@Override
	public void onTransformLine(long lineNumber, StringBuilder lineBuffer, String[] fields) throws TFileException {
		StringWriter outBuffer = new StringWriter();
		try (CsvWriter w = new CsvWriter(outBuffer)) {
			w.setDelimiter(targetParam.getDelimiter());
			w.setTextQualifier(targetParam.getEnclosing());
			w.setEscapeMode(BACKSLASH);
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].contains("\n")) {
					fields[i] = StringUtils.remove(fields[i], "\n");
				}
				if (fields[i].contains("\r")) {
					fields[i] = StringUtils.remove(fields[i], "\r");
				}
				w.write(fields[i], true);
			}
			super.copyLine(outBuffer.toString(), lineBuffer);
		} catch (IOException e) {
			throw new TFileException(e);
		}

	}

	// public static void test2() throws IOException {
	// CsvReader r = new CsvReader(getReader());
	// r.setDelimiter(',');
	// r.setTextQualifier('"');
	// r.setEscapeMode(CsvReader.ESCAPE_MODE_DOUBLED);
	//
	// CsvWriter w = new CsvWriter(getWriter(), ',');
	// w.setDelimiter(',');
	// w.setTextQualifier('"');
	// w.setEscapeMode(CsvReader.ESCAPE_MODE_BACKSLASH);
	//
	// while (r.readRecord()) {
	// String[] values = r.getValues();
	// for (int i = 0; i < values.length; i++) {
	// if (values[i].contains("\n")) {
	// // System.out.println(r.getRawRecord());
	// values[i] = StringUtils.delete(values[i], "\n");
	// }
	// if (values[i].contains("\r")) {
	// // System.out.println(r.getRawRecord());
	// values[i] = StringUtils.delete(values[i], "\r");
	// }
	// }
	// w.writeRecord(values);
	// }
	// r.close();
	// w.close();
	// }

	// public Csv2CsvFilter(CsvParam sourceParam, CsvParam targetParam) {
	// super();
	// this.sourceParam = sourceParam;
	// this.targetParam = targetParam;
	// if (sourceHasEscapeUnquotedValues) targetEscapeUnquotedValues=true;
	// targetMetaChar = String.valueOf(targetParam.getDelimiter()) +
	// String.valueOf(targetParam.getEnclosing())
	// + String.valueOf(targetParam.getEscape());
	// }
	//
	// private boolean isEnclosing = false;
	//// private boolean isEmptyEnclosing = false;
	// private boolean isNotEnclosing = false;
	// private boolean isEscaping = false;
	// private boolean isStartingField = false;
	//
	// @Override
	// public void onTransformLine(final long lineNumber, final StringBuilder
	// sourceLine) throws TFileException {
	// isEnclosing = false;
	// isNotEnclosing = !isEnclosing;
	// isEscaping = false;
	// isStartingField = true;
	//// isEmptyEnclosing = false;
	// fieldCounter = 0;
	// fieldPos[fieldCounter]=-1;
	// char currCh = EMPTY_CHAR;
	// char nextCh = EMPTY_CHAR;
	// super.emptyLine(targetLine);
	//
	//// logger.debug( lineNumber + ">" + sourceLine);
	// for (int i = 0; i < sourceLine.length(); i++) {
	// currCh = sourceLine.charAt(i);
	// if (i + 1 < sourceLine.length())
	// nextCh = sourceLine.charAt(i + 1);
	// else
	// nextCh = EMPTY_CHAR;
	// processChar(lineNumber, i, currCh, nextCh, targetLine);
	// }
	//// logger.debug( lineNumber + ">" + targetLine);
	// super.copyLine(targetLine, sourceLine);
	//
	// }
	//
	// private void setField(int pos) {
	// fieldCounter++;
	// fieldPos[fieldCounter]=pos;
	//// if (fieldCounter>0) {
	//// logger.debug("[" + targetLine.substring(fieldPos[fieldCounter-1]+1,
	// fieldPos[fieldCounter]) + "]");
	//// }
	// }
	//
	// private void processChar(long lineNumber, int position, char ch, char
	// nextCh, StringBuilder targetLine)
	// throws TFileException {
	//
	// isNotEnclosing = !isEnclosing;
	// if (isEnclosing) {
	// if (isEscaping) {
	// isEscaping = false;
	// if (ch == targetParam.getEnclosing()) {
	// targetLine.append(targetParam.getEscape());
	// }
	// targetLine.append(ch);
	// return;
	// }
	// //handle empty enclosing
	//// if (isEmptyEnclosing) {
	//// isEmptyEnclosing=false;
	//// isEnclosing = false;
	//// targetLine.append(targetParam.getEnclosing());
	////// setField(targetLine.length());
	//// return;
	//// }
	// // stop enclosing
	// if (!isStartingField && ch == sourceParam.getEnclosing()
	// && (nextCh == sourceParam.getDelimiter() || nextCh == EMPTY_CHAR)) {
	// isEnclosing = false;
	// targetLine.append(targetParam.getEnclosing());
	// return;
	// }
	// // escaping
	// if (!isStartingField && ch == sourceParam.getEscape()) {
	// isEscaping = true;
	// return;
	// }
	// if (ch == targetParam.getEscape() || ch == targetParam.getEnclosing()) {
	// targetLine.append(targetParam.getEscape());
	// targetLine.append(ch);
	// return;
	// }
	// }
	//
	// if (isNotEnclosing) {
	// if (sourceHasEscapeUnquotedValues) {
	// if (isEscaping) {
	// isEscaping = false;
	// if (targetEscapeUnquotedValues && targetMetaChar.indexOf(ch) > -1) {
	// targetLine.append(targetParam.getEscape());
	// }
	// targetLine.append(ch);
	// return;
	// }
	// // check escaping
	// if (!isStartingField && ch == sourceParam.getEscape()) {
	// isEscaping = true;
	// return;
	// }
	// }
	// // check start field
	// if (!isStartingField && ch == sourceParam.getDelimiter()) {
	// isStartingField = true;
	// setField(targetLine.length());
	// targetLine.append(targetParam.getDelimiter());
	// return;
	// }
	// // check empty field
	// if (isStartingField && ch == sourceParam.getDelimiter()) {
	// isStartingField = true;
	// if (targetReplaceEmptyValue) {
	// targetLine.append(targetEmptyValue);
	// }
	// setField(targetLine.length());
	// targetLine.append(targetParam.getDelimiter());
	// return;
	// }
	// // check start enclosing
	// if (isStartingField && ch == sourceParam.getEnclosing()) {
	// //check empty enclosing field
	// isStartingField = false;
	// isEnclosing = true;
	//// if (nextCh == sourceParam.getEnclosing()) {
	//// isEmptyEnclosing=true;
	//// } else {
	//// isEmptyEnclosing=false;
	//// }
	// targetLine.append(targetParam.getEnclosing());
	// return;
	// }
	// // check if escape target value
	// if (targetEscapeUnquotedValues) {
	// if (!isStartingField && targetMetaChar.indexOf(ch) > -1) {
	// isStartingField = false;
	// targetLine.append(targetParam.getEscape());
	// targetLine.append(ch);
	// return;
	// }
	// }
	// isStartingField = false;
	// }
	// targetLine.append(ch);
	// }

}
