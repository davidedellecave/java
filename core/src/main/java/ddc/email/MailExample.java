package ddc.email;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.pop3.POP3Client;
import org.apache.commons.net.pop3.POP3MessageInfo;
import org.apache.commons.net.pop3.POP3SClient;


public class MailExample {
	public static void main(String[] args) throws EmailException {
//		exSend();
		LiteMailConfig c = new LiteMailConfig();
		c.setPopHost("mail.delle-cave.it");
		c.setUsername("davide@delle-cave.it");
		c.setPassword(args[0]);
		exRead(c);
		
	}
	
	private static void exSend() throws EmailException {
		System.out.println("Mail sending...");
		Email email = new SimpleEmail();
		email.setHostName("mail.delle-cave.it");
		email.setSmtpPort(25);
		email.setAuthenticator(new DefaultAuthenticator("davide@delle-cave.it", null));
//		email.setSSLOnConnect(true);
		email.setFrom("test-sender@gmail.com");
		email.setSubject("TestMail");
		email.setMsg("This is a test mail ... :-)");
		email.addTo("davide.dellecave@gmail.com");
		email.send();
		System.out.println("Mail sent");
	}
	
	private static void exRead(LiteMailConfig config) throws EmailException {
		String server = config.getPopHost();
		String username = config.getUsername();
		String password = config.getPassword();

//		String proto = config.getProto args.length > 3 ? args[3] : null;
		String proto = null;
//		boolean implicit = args.length > 4 ? Boolean.parseBoolean(args[4]) : false;
		boolean implicit = false;
		
		POP3Client pop3;
		if (proto != null) {
			System.out.println("Using secure protocol: " + proto);
			pop3 = new POP3SClient(proto, implicit);
		} else {
			pop3 = new POP3Client();
		}
		 System.out.println("Connecting to server "+server+" on "+pop3.getDefaultPort());
		 
		// We want to timeout if a response takes longer than 60 seconds
		pop3.setDefaultTimeout(60000);

		// suppress login details
		pop3.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));

		try {
			pop3.connect(server);
		} catch (IOException e) {
			System.err.println("Could not connect to server.");
			e.printStackTrace();
			System.exit(1);
		}
		
		try
        {
            if (!pop3.login(username, password))
            {
                System.err.println("Could not login to server.  Check password.");
                pop3.disconnect();
                System.exit(1);
            }

            POP3MessageInfo[] messages = pop3.listMessages();

            if (messages == null)
            {
                System.err.println("Could not retrieve message list.");
                pop3.disconnect();
                return;
            }
            else if (messages.length == 0)
            {
                System.out.println("No messages");
                pop3.logout();
                pop3.disconnect();
                return;
            }

            for (POP3MessageInfo msginfo : messages) {
                BufferedReader reader = (BufferedReader) pop3.retrieveMessageTop(msginfo.number, 0);

                if (reader == null) {
                    System.err.println("Could not retrieve message header.");
                    pop3.disconnect();
                    System.exit(1);
                }
                printMessageInfo(reader, msginfo.number);
            }

            pop3.logout();
            pop3.disconnect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
	}
	
    public static final void printMessageInfo(BufferedReader reader, int id) throws IOException  {
        String from = "";
        String subject = "";
        String line;
        while ((line = reader.readLine()) != null)
        {
        	System.out.println(line);
            String lower = line.toLowerCase(Locale.ENGLISH);
            if (lower.startsWith("from: ")) {
                from = line.substring(6).trim();
            }  else if (lower.startsWith("subject: ")) {
                subject = line.substring(9).trim();
            }
        }
        System.out.println("\n\n------------\n\n");
    }
}
