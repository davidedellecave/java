package ddc.util;

import java.util.concurrent.TimeUnit;

public class Timespan {
	private long millis = 0;

	public Timespan() {
	}

	public Timespan(long millis) {
		this.millis = millis;
	}

	public Timespan(long duration, TimeUnit unit) {
		this.millis = TimeUnit.MILLISECONDS.convert(duration, unit);
	}

	public long getMillis() {
		return millis;
	}

	public void setMillis(long millis) {
		this.millis = millis;
	}

	public TimeUnit getUnit() {
		return TimeUnit.MILLISECONDS;
	}

	public long getValue() {
		return millis;
	}

	/**
	 * Returm the spantime representation using specified timespan unit
	 * 
	 * @param newUnit
	 * @return
	 */
	public long getValue(TimeUnit newUnit) {
		// For example, to convert 10 minutes to milliseconds, use:
		// TimeUnit.MILLISECONDS.convert(10L, TimeUnit.MINUTES)
		return newUnit.convert(millis, TimeUnit.MILLISECONDS);
	}

	public void sub(Timespan ts) {
		this.setMillis(getMillis() - ts.getMillis());
	}

	public void add(Timespan ts) {
		this.setMillis(getMillis() + ts.getMillis());
	}

	public static String getHumanReadable(long duration, TimeUnit unit) {
		long millis = TimeUnit.MILLISECONDS.convert(duration, unit);
		return getHumanReadable(millis);

	}

	// public static String getHumanReadable(long millis) {
	// if (millis < 0) {
	// throw new IllegalArgumentException(
	// "Duration must be greater than zero!");
	// }
	// long days = TimeUnit.MILLISECONDS.toDays(millis);
	// millis -= TimeUnit.DAYS.toMillis(days);
	//
	// long hours = TimeUnit.MILLISECONDS.toHours(millis);
	// millis -= TimeUnit.HOURS.toMillis(hours);
	//
	// long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
	// millis -= TimeUnit.MINUTES.toMillis(minutes);
	//
	// long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
	// millis -= TimeUnit.SECONDS.toMillis(seconds);
	//
	// StringBuilder sb = new StringBuilder(64);
	//
	//// long[] tSeries = new long[]{days, hours, minutes, seconds};
	//// int p1 = 0;
	//// int p2 = 3;
	//// int i = 0;
	//// while (tSeries[i]==0 && i<4) {
	//// i++;
	//// p1++;
	//// }
	//// i=3;
	//// while (tSeries[i]==0 && i>-1) {
	//// i--;
	//// p2--;
	//// }
	//// if (p2<=p1) p2=p1+1;
	//
	//
	//
	// if (days > 0) {
	// sb.append(days);
	// sb.append(" days ");
	// }
	//
	// if (days > 0 || hours > 0) {
	// sb.append(hours);
	// sb.append("h ");
	// }
	//
	// if (days > 0 || hours > 0 || minutes > 0) {
	// sb.append(minutes);
	// sb.append("' ");
	// }
	//
	// if (days > 0 || hours > 0 || minutes > 0 || seconds > 0) {
	// sb.append(seconds);
	// if (millis>0) {
	// sb.append(". ");
	// sb.append(millis);
	// }
	// sb.append("'' ");
	// } else {
	// sb.append(millis);
	// sb.append("ms ");
	// }
	// return (sb.toString());
	// }

    public static String getHumanReadable(long elapsed) {
        long elapsedHours = elapsed / (60 * 60 * 1000);
        long elapsedMinutes = (elapsed - elapsedHours * 60 * 60 * 1000) / (60 * 1000);
        long elapsedSeconds = (elapsed - elapsedHours * 60 * 60 * 1000 - elapsedMinutes * 60 * 1000) / 1000;
        long elapsedMillis = elapsed - elapsedHours * 60 * 60 * 1000 - elapsedMinutes * 60 * 1000 - elapsedSeconds * 1000;

        return String.format("%d:%02d:%02d:%03d", elapsedHours, elapsedMinutes, elapsedSeconds, elapsedMillis);
    }
    
//	public static String getHumanReadable(long millis) {
//		if (millis < 0) {
//			throw new IllegalArgumentException("Duration must be greater than zero!");
//		}
//		long days = TimeUnit.MILLISECONDS.toDays(millis);
//		millis -= TimeUnit.DAYS.toMillis(days);
//
//		long hours = TimeUnit.MILLISECONDS.toHours(millis);
//		millis -= TimeUnit.HOURS.toMillis(hours);
//
//		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
//		millis -= TimeUnit.MINUTES.toMillis(minutes);
//
//		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
//		millis -= TimeUnit.SECONDS.toMillis(seconds);
//
//		StringBuilder sb = new StringBuilder(64);
//
//		if (days > 0) {
//			sb.append(days);
//			sb.append(" days");
//		}
//
//		if (hours > 0 || (days > 0 && (minutes > 0 || seconds > 0))) {
//			sb.append(" " + hours);
//			sb.append("h");
//		}
//
//		if (minutes > 0 || ((days > 0 || hours > 0) && seconds > 0)) {
//			sb.append(" " + minutes);
//			sb.append("'");
//		}
//
//		if (seconds > 0) {
//			sb.append(" " + seconds);
//			sb.append("''");
//			if (millis > 0) {
//				sb.append(".");
//				sb.append(millis);
//			}
//		} else if (millis > 0) {
//			sb.append(millis);
//			sb.append("ms");
//		}
//
//		return (sb.toString().trim());
//	}

	public static Timespan createTimespan(long duration, TimeUnit unit) {
		return new Timespan(duration, unit);
	}

	public String toStringValue() {
		return String.valueOf(millis);
	}

}
