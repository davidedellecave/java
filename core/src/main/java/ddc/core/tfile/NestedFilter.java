package ddc.core.tfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Hold a list of TFileFilter in order to process the source If source is empty
 * the next filter is not called
 * 
 * @author dellecave
 *
 */
public class NestedFilter implements TFileFilter, TFileContextListener {

	private List<TFileFilter> list = new ArrayList<>();

	public NestedFilter() {
		// this.filter=filter;
	}

	public NestedFilter add(TFileFilter filter) {
		list.add(filter);
		return this;
	}

	@Override
	public StringBuilder onStartLine(long lineNumber, StringBuilder lineBuffer) throws TFileException {
		for (TFileFilter filter : list) {
			lineBuffer = filter.onStartLine(lineNumber, lineBuffer);
		}
		return lineBuffer;
	}

	@Override
	public char onChar(long lineNumber, long position, char ch) throws TFileException {
		for (TFileFilter filter : list) {
			if (ch != '\0')
				ch = filter.onChar(lineNumber, position, ch);
		}
		return ch;
	}

	@Override
	public StringBuilder onEndLine(long lineNumber, StringBuilder lineBuffer) throws TFileException {
		for (TFileFilter filter : list) {
			if (lineBuffer.length() > 0)
				lineBuffer = filter.onEndLine(lineNumber, lineBuffer);
		}
		return lineBuffer;
	}

	@Override
	public void onOpen(TFileContext context) throws TFileException {
		for (TFileFilter filter : list) {
			if (filter instanceof TFileContextListener) {
				((TFileContextListener) filter).onOpen(context);
			}
		}
	}

	@Override
	public void onClose(TFileContext context) throws TFileException {
		for (TFileFilter filter : list) {
			if (filter instanceof TFileContextListener) {
				((TFileContextListener) filter).onClose(context);
			}
		}
	}

	@Override
	public void onError(TFileLineError error) throws TFileException {
		for (TFileFilter filter : list) {
			filter.onError(error);
		}
	}

	// @Override
	// public void onErrorLine(List<TFileLineError> errors) throws
	// TFileException {
	// for (TFileFilter filter : list) {
	// filter.onErrorLine(errors);
	// }
	// }

}