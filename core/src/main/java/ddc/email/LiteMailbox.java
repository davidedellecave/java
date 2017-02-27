package ddc.email;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.mail.EmailException;
import org.apache.commons.net.pop3.POP3Client;
import org.apache.commons.net.pop3.POP3MessageInfo;

import ddc.email._Obsolete_MailFetcher.MailInfo;
import ddc.util.Statistics;

public class LiteMailbox {
	private Statistics stats = new Statistics();

	public Statistics getStats() {
		return stats;
	}

	public void scan(LiteMailConfig config, MailHeaderParser parser, MailHeaderFilter filter, MailEval action) throws Throwable {
		doScan(config, parser, filter, action);
	}
	
	public void scan(LiteMailConfig config, MailHeaderFilter filter, MailEval action) throws Throwable {
		MailHeaderParser parser = new DefaultMailHeaderParser();
		doScan(config, parser, filter, action);
	}

	public void scan(LiteMailConfig config, MailEval action) throws Throwable {
		MailHeaderParser parser = new DefaultMailHeaderParser();
		AcceptMailHeaderFilter filter = new AcceptMailHeaderFilter();
		doScan(config, parser, filter, action);
	}

	public void scan(LiteMailConfig config, MailHeaderParser parser) throws Throwable {
		AcceptMailHeaderFilter filter = new AcceptMailHeaderFilter();
		DummyMailEval eval = new DummyMailEval();
		doScan(config, parser, filter, eval);
	}
	
	
	private void loadMessage(POP3Client pop3, MailInfo mailInfo) throws IOException {
		BufferedReader reader = (BufferedReader) pop3.retrieveMessage(mailInfo.id);
		String line = null;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}

	}
	
	private void doScan(LiteMailConfig config, MailHeaderParser parser, MailHeaderFilter filter, MailEval action) throws Throwable {
		POP3Client client = login(config);
		if (client != null) {
			try {
				POP3MessageInfo[] messages = client.listMessages();
				log("Messages size:[" + messages.length + "]");
				int simulationCounter = 0;
				if (messages.length == 0) {
					return;
				}
				MailHeader header = null;
				for (POP3MessageInfo msginfo : messages) {
					BufferedReader reader = (BufferedReader) client.retrieveMessageTop(msginfo.number, 0);
					if (reader == null) {
						log("Could not retrieve message header.");
						return;
					}
					try {
						header = parseHeaderProxy(config, reader, msginfo.number, parser);
					} finally {
						if (reader != null)
							reader.close();
					}
					stats.itemsProcessed++;
					if (header==null) {
						log("header is", "null", "id", String.valueOf(msginfo.number));
						break;
					}
					if (config.getMailboxLimit() > 0 && stats.itemsProcessed >= config.getMailboxLimit()) {
						log("Reached the limt of messages to read - limit", config.getMailboxLimit());
						break;
					}
					boolean isSelected = filter.filter(config, header);
					if (config.isMailboxSimulation()) {
						if (isSelected) {
							simulationCounter++;
							log("Simulation is on - selected", header);
						} else {
							log("Simulation is on - NOT selected", header);
						}
					} else if (isSelected) {
						stats.itemsAffected += action.execute(config, client, header);
					}
				}
				if (config.isMailboxSimulation()) {
					log("Simulation is on - counter", simulationCounter);
				}
			} finally {
				client.logout();
				client.disconnect();
			}
		}
	}

	private final MailHeader parseHeaderProxy(LiteMailConfig config, BufferedReader reader, int messageId, MailHeaderParser parser) throws Throwable {
		String line;
		MailHeader header = new MailHeader(messageId);
		while ((line = reader.readLine()) != null) {
			parser.parse(config, messageId, line, header);
		}
		return header;
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

	private void log(String title, Object message) {
		System.out.println(title + ":[" + message + "]");
	}

//	private void logElapsed(String title, int message) {
//		System.out.println(title + ":[" + message + "] - elapsed:[" + stats.chron.toString() + "]");
//	}

	public void log(String... args) {
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

	public void logElapsed(String... args) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			if (i % 2 == 0) {
				b.append(args[i]);
			} else {
				b.append(":[" + args[i] + "] ");
			}
		}
		b.append(" - elpased:[" + stats.chron.toString() + "] ");
		System.out.println(b);
	}
}
