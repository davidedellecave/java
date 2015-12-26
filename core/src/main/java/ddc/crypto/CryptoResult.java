package ddc.crypto;
/**
 * @author davidedc 2014
 *
 */

import ddc.task1.TaskInfo;

public class CryptoResult extends TaskInfo {
	public String data;
    
    @Override
    public String toString() {
    	StringBuffer buff = new StringBuffer(256);    
    	buff.append(" data:[").append(data).append("]");
    	buff.append(" exitCode:[").append(this.getExitCode().toString()).append("]");
    	buff.append(" exitMessage:[").append(this.getExitMessage()).append("]");
    	return buff.toString();
    }
}
