package ddc.bm.app;

public class AppTaskException extends Exception {
	private static final long serialVersionUID = 1337110466956461344L;

	public AppTaskException(String message) {
        super(message);
    }

    public AppTaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppTaskException(Throwable cause) {
        super(cause);
    }
}
