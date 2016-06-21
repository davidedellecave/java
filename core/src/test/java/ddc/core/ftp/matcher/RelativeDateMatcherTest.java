package ddc.core.ftp.matcher;

import static org.junit.Assert.*;

import org.junit.Test;

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
