package ddc.util;

public class ParseUtils {
	
	public static Double parseDouble(Object value) {
		try {
			return Double.parseDouble(String.valueOf(value));
		} catch(NumberFormatException e) {
			System.err.println("parseDouble() Exception:[" + e.getMessage() + "]");
			return (double)0;
		}
	}

	public static Float parseFloat(Object value) {
		try {
			return Float.parseFloat(String.valueOf(value));
		} catch(NumberFormatException e) {
			System.err.println("parseFloat() Exception:[" + e.getMessage() + "]");
			return (float)0;
		}
	}
	
	public static Long parseLong(Object value) {
		try {
			return Long.parseLong(String.valueOf(value));
		} catch(NumberFormatException e) {
			System.err.println("parseLong() Exception:[" + e.getMessage() + "]");
			return (long)0;
		}
	}



}
