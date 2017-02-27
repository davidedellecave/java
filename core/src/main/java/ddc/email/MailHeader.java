package ddc.email;

import java.text.ParseException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.TreeMap;

import ddc.util.DateUtil;

public class MailHeader {
	public enum MailProp {
		from, to, cc, bcc, subject, date
	};

	private static String[] datePattern = new String[] { "EEE, d MMM yyyy HH:mm:ss Z", "d MMM yyyy HH:mm:ss Z" };

	public int id = 0;
	public Map<String, String> props = new TreeMap<>();

	public MailHeader(int id) {
		super();
		this.id = id;
	}

	public ZonedDateTime getDate() throws ParseException {
		if (props.containsKey(MailProp.date.name())) {
			String v = props.get(MailProp.date.name());
			Instant i = DateUtil.parseToInstant(v, datePattern);
			ZonedDateTime zdt = DateUtil.toZonedDateTime(i.toEpochMilli());
			return zdt;
		}
		return null;
	}
	
	public Map<String, String> getProps() {
		return props;
	}

	public void setProps(Map<String, String> props) {
		this.props = props;
	}

	public String getFrom() {
		if (props.containsKey(MailProp.from.name())) {
			return (String) props.get(MailProp.from.name());
		}
		return null;
	}

	public String getsubject() {
		if (props.containsKey(MailProp.subject.name())) {
			return (String) props.get(MailProp.subject.name());
		}
		return null;
	}

//	public String parseLine(String line) {
//		for (MailProp p : MailProp.values()) {
//			String key = p.name().toLowerCase();
//			line = line.toLowerCase();
//			if (line.startsWith(key)) {
//				// log(line);
//				int pos = key.length() + 2;
//				if (line.length() >= pos) {
//					String content = line.substring(pos).trim();
//					props.put(key, content);
//				} else {
//					props.put(key, "");
//				}
//			}
//		}
//		return null;
//	}

	@Override
	public String toString() {
		return "id:[" + id + "] props:[" + props.toString() + "]";
	}
}
