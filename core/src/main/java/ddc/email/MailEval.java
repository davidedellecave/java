package ddc.email;

import org.apache.commons.net.pop3.POP3Client;

public interface MailEval {
	public int execute(LiteMailConfig config, POP3Client client, MailHeader header) throws Throwable;

}
