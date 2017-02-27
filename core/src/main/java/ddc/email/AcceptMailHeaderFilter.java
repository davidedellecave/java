package ddc.email;

public class AcceptMailHeaderFilter implements MailHeaderFilter{

	@Override
	public boolean filter(LiteMailConfig config, MailHeader header) throws Throwable {
		return true;
	}

}
