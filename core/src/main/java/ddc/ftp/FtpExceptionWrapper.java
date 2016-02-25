package ddc.ftp;

public class FtpExceptionWrapper extends Exception {
	private static final long serialVersionUID = 1L;
	
    public FtpExceptionWrapper(String message) {
        super(message);
    }
    
    public FtpExceptionWrapper(Throwable cause) {
        super(cause);
    }
    
    public FtpExceptionWrapper(String message, Throwable cause) {
        super(message, cause);
    }

}
