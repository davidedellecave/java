package ddc.email;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;


public class Example {
	public static void main(String[] args) throws EmailException {
		ex();
	}
	
	private static void ex() throws EmailException {
		System.out.println("Mail sending...");
		Email email = new SimpleEmail();
		email.setHostName("mail.delle-cave.it");
		email.setSmtpPort(25);
		email.setAuthenticator(new DefaultAuthenticator("davide@delle-cave.it", "Quasar-2"));
//		email.setSSLOnConnect(true);
		email.setFrom("test-sender@gmail.com");
		email.setSubject("TestMail");
		email.setMsg("This is a test mail ... :-)");
		email.addTo("davide.dellecave@gmail.com");
		email.send();
		System.out.println("Mail sent");
	}
}
