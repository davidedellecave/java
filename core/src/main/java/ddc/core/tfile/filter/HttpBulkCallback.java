package ddc.core.tfile.filter;

import ddc.core.tfile.TFileException;

public interface HttpBulkCallback {
	public void notifyStart(long size) throws TFileException;
	public void notifyBulkProcessed(String[] lines) throws TFileException;
}
