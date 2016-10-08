package ddc.bm.conf;

public class ConfigurationException extends RuntimeException {
	private static final long serialVersionUID = -2224368451053234333L;

	public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }
}
