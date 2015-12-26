package ddc.quickcipher;

public class CipherResult {
    private boolean hasError = false;
    private String error;
    private String data;
	
    public CipherResult(String data, String errMessage) {
		if (errMessage != null && errMessage.length() > 0)
			this.hasError = true;
		this.error = errMessage;
		this.data = data;
	}
	
	public boolean hasError() {
		return hasError;
	}
	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
    
    

}
