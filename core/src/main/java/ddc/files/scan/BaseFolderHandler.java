package ddc.files.scan;

import java.io.File;

import ddc.files.scan.ScanFolder.ScanResult;

public class BaseFolderHandler implements ScanFolderHandler {
	public ScanResult handleFile(File file, ContextScan ctx) {
		return ScanResult.continueScan;
	}
	
	public ScanResult preHandleFolder(File folder, ContextScan ctx)  {
		return ScanResult.continueScan;
	}
	
	public ScanResult postHandleFolder(File folder, ContextScan ctx) {
		return ScanResult.continueScan;
	}

	@Override
	public ScanResult startScan(ContextScan ctx) throws Exception {
		return ScanResult.continueScan;
	}

	@Override
	public ScanResult endScan(ContextScan ctx) throws Exception {
		return ScanResult.continueScan;
	}
}
