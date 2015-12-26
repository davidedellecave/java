package ddc.email;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

public class EmailClientWrapper {
	
	public void send(EmailConfigWrapper config) throws EmailExceptionWrapper {
		try {
			doSend(config);
		} catch (EmailException e) {
			throw new EmailExceptionWrapper(e);
		}
	}
	
	private void doSend(EmailConfigWrapper config) throws EmailException {
		Email email = new SimpleEmail();
		if (config.isHtmlEnabled()) {
			email = new HtmlEmail();		 
		}
		email.setHostName(config.getSmtpHost());
		if (config.getPort()!=-1) email.setSmtpPort(config.getPort());
		email.setAuthenticator(new DefaultAuthenticator(config.getUsername(), config.getPassword()));
		email.setSSLOnConnect(config.isSsl());
		email.setFrom(config.getFrom());
		for (String t : config.getTo()) {
			email.addTo(t);
		}
		for (String t : config.getCc()) {
			email.addCc(t);
		}
		for (String t : config.getBcc()) {
			email.addBcc(t);
		}		
		email.setSubject(config.getSubject());
		email.setMsg(config.getBody());
		email.send();
	}
}
