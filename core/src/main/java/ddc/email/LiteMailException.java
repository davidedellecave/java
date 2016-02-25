package ddc.email;

public class LiteMailException extends Exception {
	private static final long serialVersionUID = 1L;
	
    public LiteMailException(String message) {
        super(message);
    }
    
    public LiteMailException(Throwable cause) {
        super(cause);
    }
    
    public LiteMailException(String message, Throwable cause) {
        super(message, cause);
    }

}
