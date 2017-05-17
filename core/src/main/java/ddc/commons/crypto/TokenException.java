package ddc.core.crypto;

public class TokenException extends Exception {
	private static final long serialVersionUID = -2224368451053234333L;

	public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenException(Throwable cause) {
        super(cause);
    }
}
