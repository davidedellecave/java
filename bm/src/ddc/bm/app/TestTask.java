package ddc.bm.app;

import java.util.concurrent.TimeUnit;

import ddc.util.Chronometer;
import ddc.util.Timespan;

public class TestTask extends AppTask<String, String> {

	@Override
	public String run(String input) throws Exception {
//		throw new Exception("dddd");
		Chronometer.sleep(Timespan.createTimespan(2,TimeUnit.SECONDS));
		return input;
	}




}
