package ddc.files.scan;

import java.io.File;

import ddc.files.scan.ScanFolder.ScanResult;

public interface ScanFolderHandler {
	public ScanResult startScan(ContextScan ctx) throws Exception;
	public ScanResult endScan(ContextScan ctx) throws Exception;
	public ScanResult handleFile(File file, ContextScan ctx) throws Exception;
	
	public ScanResult preHandleFolder(File folder, ContextScan ctx) throws Exception;
	
	public ScanResult postHandleFolder(File folder, ContextScan ctx) throws Exception;
}
