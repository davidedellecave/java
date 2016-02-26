package ddc.core.tfile.filter;

import java.util.concurrent.TimeUnit;

import ddc.core.tfile.TFileException;
import ddc.util.Chronometer;

public class ElapsedFilter extends BaseTFileFilter {
	private Chronometer chron;

	public ElapsedFilter() {
		chron = new Chronometer(5, TimeUnit.SECONDS);
	}

	@Override
	public StringBuilder onEndLine(long lineNumber, StringBuilder lineBuffer) throws TFileException {
		if (chron.isCountdownCycle()) {
			String info = getContext().toString();
			System.out.println("Processing..." + info);
		}
		return lineBuffer;
	}

}
