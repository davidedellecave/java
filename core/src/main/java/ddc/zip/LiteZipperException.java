package ddc.zip;

public class LiteZipperException extends Exception {
	private static final long serialVersionUID = 1L;

	public LiteZipperException(String message) {
		super(message);
	}

	public LiteZipperException(Throwable cause) {
		super(cause);
	}

	public LiteZipperException(String message, Throwable cause) {
		super(message, cause);
	}

}
