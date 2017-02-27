package ddc.email;

public interface MailHeaderFilter {
	public boolean filter(LiteMailConfig config, MailHeader header) throws Throwable;
}
