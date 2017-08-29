package ddc.util;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.Test;

public class DateUtilTest {

	@Test
	public void testParseToDateStringString() throws ParseException {
		String dateToParse ="Wed, 16 Aug 2017 07:20:38 +0001";
		
		DateUtil.parseToDate(dateToParse, "EEE, d MMM yyyy HH:mm:ss Z");
		
	}

}
