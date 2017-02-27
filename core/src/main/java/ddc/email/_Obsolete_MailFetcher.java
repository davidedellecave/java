package ddc.email;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;
import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.mail.EmailException;
import org.apache.commons.net.pop3.POP3Client;
import org.apache.commons.net.pop3.POP3MessageInfo;

import ddc.util.Chronometer;
import ddc.util.DateUtil;

public class _Obsolete_MailFetcher {
	private Chronometer chron = new Chronometer();
	private static ZoneId ZONEID = ZoneId.of("Europe/Rome");

	public static void main(String[] args) throws EmailException, IOException, ParseException {
		_Obsolete_MailFetcher mf = new _Obsolete_MailFetcher();
		// delDelleCave(true);
		delGottardo(true);
	}

	private static void delDelleCave(boolean delete) throws EmailException, IOException {
		LiteMailConfig c = new LiteMailConfig();
		c.setPopHost("mail.delle-cave.it");
		c.setUsername("davide@delle-cave.it");
		c.setPassword("Quasar-2");

		_Obsolete_MailFetcher mf = new _Obsolete_MailFetcher();
		POP3Client client = mf.login(c);
		if (client != null) {
			try {
				ZonedDateTime olderThan = ZonedDateTime.parse("2016-12-31T23:59:59+02:00[Europe/Rome]");
				String from = "info@medisportgottardo.it";
				int limit = 1000;
				int affected = 0;
				List<MailInfo> listToDelete = mf.buildListToDelete(client, olderThan, from, limit);
				affected = mf.delete(client, listToDelete, delete);
				mf.logElapsed("deleted", affected);
			} finally {
				client.logout();
				client.disconnect();
			}
		}
	}

	private static void delGottardo(boolean delete) throws EmailException, IOException {
		LiteMailConfig c = new LiteMailConfig();
		c.setPopHost("mail.medisportgottardo.it");
		c.setUsername("info@medisportgottardo.it");
		c.setPassword("Dica-Trenta3");

		_Obsolete_MailFetcher mf = new _Obsolete_MailFetcher();
		POP3Client client = mf.login(c);
		if (client != null) {
			try {
				ZonedDateTime olderThan = ZonedDateTime.parse("2016-12-31T23:59:59+02:00[Europe/Rome]");
				String from = "info@medisportgottardo.it";
				int limit = 500;
				int affected = 0;
				List<MailInfo> listToDelete = mf.buildListToDelete(client, olderThan, from, limit);
				affected = mf.delete(client, listToDelete, delete);
				mf.logElapsed("deleted", affected);
			} finally {
				client.logout();
				client.disconnect();
			}
		}
	}

	private List<MailInfo> buildListToDelete(POP3Client client, ZonedDateTime olderThan, String from, int limit) throws IOException {
		List<MailInfo> list = loadHeader(client, limit);
		logElapsed("Header mail - size", list.size());

		List<MailInfo> filtered = list.stream().filter(x -> x.getDate().isBefore(olderThan) && x.getFrom().equals(from)).collect(Collectors.toList());
		log("Filtered mail - size", list.size());
		//
		return filtered;
	}

	private int delete(POP3Client client, List<MailInfo> list, boolean delete) throws IOException {
		//
		int counter = 0;
		for (MailInfo info : list) {
			if (delete) {
				boolean succedeed = client.deleteMessage(info.id);
				logElapsed("deleting...", String.valueOf(succedeed), "mail", info.toString());
				if (succedeed)
					counter++;
			} else {
				log("simulation >>>>", info);
			}
		}
		return counter;
	}
	


	private void log(String title, Object message) {
		System.out.println(title + ":[" + message + "]");
	}

	private void logElapsed(String title, int message) {
		System.out.println(title + ":[" + message + "] - elapsed:[" + chron.toString() + "]");
	}

	private void log(String... args) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			if (i % 2 == 0) {
				b.append(args[i]);
			} else {
				b.append(":[" + args[i] + "] ");
			}
		}
		System.out.println(b);
	}

	private void logElapsed(String... args) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			if (i % 2 == 0) {
				b.append(args[i]);
			} else {
				b.append(":[" + args[i] + "] ");
			}
		}
		b.append(" - elpased:[" + chron.toString() + "] ");
		System.out.println(b);
	}

	private void loadMessage(POP3Client pop3, MailInfo mailInfo) throws IOException {
		BufferedReader reader = (BufferedReader) pop3.retrieveMessage(mailInfo.id);
		String line = null;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}

	}

	private POP3Client login(LiteMailConfig config) throws EmailException, IOException {
		String server = config.getPopHost();
		String username = config.getUsername();
		String password = config.getPassword();
		// String proto = config.getProto args.length > 3 ? args[3] : null;
		// String proto = null;
		// boolean implicit = args.length > 4 ? Boolean.parseBoolean(args[4]) :
		// false;
		// boolean implicit = false;
		POP3Client pop3 = new POP3Client();
		// if (proto != null) {
		// System.out.println("Using secure protocol: " + proto);
		// pop3 = new POP3SClient(proto, implicit);
		// } else {
		// pop3 = new POP3Client();
		// }
		log("Connecting to server " + server + " on " + pop3.getDefaultPort() + " ...");

		// We want to timeout if a response takes longer than 60 seconds
		pop3.setDefaultTimeout(60000);

		// suppress login details
		try {
			// pop3.addProtocolCommandListener(new PrintCommandListener(new
			// PrintWriter(System.out), true));
			pop3.connect(server);
			log("Connected to server " + server + " on " + pop3.getDefaultPort());
			boolean isAuth = pop3.login(username, password);
			log("Logged in");
			if (isAuth)
				return pop3;
			else {
				pop3.disconnect();
				return null;
			}
		} catch (SocketException e) {
			if (pop3 != null)
				pop3.disconnect();
			return null;
		}
	}

	private List<MailInfo> loadHeader(POP3Client pop3, int limit) throws IOException {
		List<MailInfo> infoList = new LinkedList<>();

		POP3MessageInfo[] messages = pop3.listMessages();
		log("Messages size:[" + messages.length + "]");

		if (messages.length == 0) {
			log("logout...");
			pop3.logout();
			pop3.disconnect();
			return infoList;
		}
		int counter = 0;
		for (POP3MessageInfo msginfo : messages) {
			BufferedReader reader = (BufferedReader) pop3.retrieveMessageTop(msginfo.number, 0);
			if (reader == null) {
				log("Could not retrieve message header.");
				pop3.disconnect();
				return infoList;
			}
			MailInfo info = parseMessageInfo(reader, msginfo.number);
			infoList.add(info);
			counter++;
			if (limit > 0 && counter == limit) {
				log("Reached the limt of messages to read - limit:[" + limit + "]");
				return infoList;
			}
		}
		return infoList;

	}

	private final MailInfo parseMessageInfo(BufferedReader reader, int id) throws IOException {
		String line;
		MailInfo mailInfo = new MailInfo(id);
		while ((line = reader.readLine()) != null) {
			mailInfo.parseLine(line);
		}
		// mailInfo.normalize();
		return mailInfo;
	}

	public enum MailProp {
		from, to, cc, bcc, subject, date
	};

	private static String[] datePattern = new String[] { "EEE, d MMM yyyy HH:mm:ss Z", "d MMM yyyy HH:mm:ss Z" };

	class MailInfo {
		public int id = 0;
		public Map<String, String> props = new TreeMap<>();

		public MailInfo(int id) {
			super();
			this.id = id;
		}

		public ZonedDateTime getDate() {
			try {
				if (props.containsKey(MailProp.date.name())) {
					String v = props.get(MailProp.date.name());
					// log("v", v);
					Instant i = DateUtil.parseToInstant(v, datePattern);
					// log("i", i);
					ZonedDateTime zdt = DateUtil.toZonedDateTime(i.toEpochMilli());
					// log("zdt", zdt);
					// java.util.Date d = DateUtil.parseToDate(v, datePattern);
					// log("d", d);
					return zdt;
				}
			} catch (Exception e) {

			}
			return null;
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

		public String parseLine(String line) {
			for (MailProp p : MailProp.values()) {
				String key = p.name().toLowerCase();
				line = line.toLowerCase();
				if (line.startsWith(key)) {
//					log(line);
					int pos = key.length() + 2;
					if (line.length() >= pos) {
						String content = line.substring(pos).trim();
						props.put(key, content);
					} else {
						props.put(key, "");
					}
				}
			}
			return null;
		}

		// public void normalize() {
		// for (Map.Entry<String, Object> entry : props.entrySet()) {
		// if (entry.getKey().equals("date")) {
		// try {
		// entry.setValue(parseDate(entry.getValue()));
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// // Date v = Date.valueOf(entry.getValue());
		// }
		// }
		// }

		// private Instant parseDate(Object date) throws ParseException {
		// return DateUtil.parseToInstant(String.valueOf(date), datePattern);
		// }

		@Override
		public String toString() {
			// StringBuffer b = new StringBuffer();
			// for (MailProp p : MailProp.values()) {
			// if (props.containsKey(p.name())) {
			// b.append(p.name() + ":[" + props.get(p.) + "] ");
			// }
			// }
			// return b.toString();
			return "id:[" + id + "] props:[" + props.toString() + "]";
		}
	}
}
