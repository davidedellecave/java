package ddc.bm.app;

public class SecurityException extends RuntimeException {
	private static final long serialVersionUID = -2224368451053234333L;

	public SecurityException(String message) {
        super(message);
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecurityException(Throwable cause) {
        super(cause);
    }
}
