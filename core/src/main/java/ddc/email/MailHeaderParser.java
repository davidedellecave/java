package ddc.email;

public interface MailHeaderParser {
	public void parse(LiteMailConfig config, int messageId, String rawLine, MailHeader header) throws Throwable;
}
