package ddc.core.tfile;

public class TFileException extends Exception {
	private static final long serialVersionUID = 1L;
    
    public TFileException(String message) {
        super(message);
    }
    
    public TFileException(Throwable cause) {
        super(cause);
    }
    
    public TFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
