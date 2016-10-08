package ddc.util;

public class ExceptionUtil {
	
	public static String getMsg(Throwable e) {
		String info = "";
		if (e != null) {
			info = e.getClass().getSimpleName();
			if (e.getMessage() != null && e.getMessage().length() > 0)
				info += " " + e.getMessage();
			else if (e.getCause() != null && e.getCause().getMessage()!=null)
				info += " " + e.getCause().getMessage();
		} else {
			info = "unknown error";
		}
		return info;
	}
}
