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
	public char onChar(long lineNumber, long position, char ch) throws TFileException {
		for (TFileFilter filter : list) {
			if (ch != '\0')
				ch = filter.onChar(lineNumber, position, ch);
		}
		return ch;
	}

	
	@Override
	public void onTransformLine(final long lineNumber, final StringBuilder sourceLine) throws TFileException {
		for (TFileFilter filter : list) {
			filter.onTransformLine(lineNumber, sourceLine);
		}
	}
	
	@Override
	public void onTransformLine(long lineNumber, StringBuilder sourceLine, String[] fields) throws TFileException {
		for (TFileFilter filter : list) {
			filter.onTransformLine(lineNumber, sourceLine, fields);
		}
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
}