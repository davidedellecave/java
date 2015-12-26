package ddc.files.scan;

import java.io.File;

import ddc.files.scan.ScanFolder.ScanResult;

public class ContinueScanFolderHandler implements ScanFolderHandler{

	@Override
	public ScanResult startScan(ContextScan ctx) throws Exception {
		return ScanResult.continueScan;
	}

	@Override
	public ScanResult endScan(ContextScan ctx) throws Exception {
		return ScanResult.continueScan;	}

	@Override
	public ScanResult handleFile(File file, ContextScan ctx) throws Exception {
		return ScanResult.continueScan;	}

	@Override
	public ScanResult preHandleFolder(File folder, ContextScan ctx)
			throws Exception {
		return ScanResult.continueScan;	}

	@Override
	public ScanResult postHandleFolder(File folder, ContextScan ctx)
			throws Exception {
		return ScanResult.continueScan;
	}

}
