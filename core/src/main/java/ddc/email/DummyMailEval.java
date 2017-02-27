package ddc.email;

import org.apache.commons.net.pop3.POP3Client;

public class DummyMailEval implements MailEval{

	@Override
	public int execute(LiteMailConfig config, POP3Client client, MailHeader header) throws Throwable {
		return 0;
	}

}
