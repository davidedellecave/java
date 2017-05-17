package ddc.core.ftp.matcher;

import org.junit.Test;

import ddc.commons.ftp.matcher.RelativeDateMatcher;

public class RelativeDateMatcherTest {

	@Test
	public void testRelativeDateMatcher() {
//		RelativeDateMatcher r = new RelativeDateMatcher("-365 DAYS, -1 DAYS");
		RelativeDateMatcher r = new RelativeDateMatcher("32323 MILLISECONDS, -453 MINUTES");
		try {
			System.out.println(r);
		} catch (Throwable  e) {
			System.out.println(e.getMessage());
		}
	}
}
