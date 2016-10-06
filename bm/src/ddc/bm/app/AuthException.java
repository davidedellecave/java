package ddc.bm.app;

public class AuthException extends RuntimeException {
	private static final long serialVersionUID = -2224368451053234333L;

	public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(Throwable cause) {
        super(cause);
    }
}
