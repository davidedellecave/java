package ddc.files.scan;

import java.io.File;

import ddc.files.scan.ScanFolder.ScanResult;

public class DefaultScanFolderHandler implements ScanFolderHandler {
	public ScanResult handleFile(File file, ContextScan ctx) {
		System.out.println("File:" + file);
		return ScanResult.continueScan;
	}
	
	public ScanResult preHandleFolder(File folder, ContextScan ctx) {
		System.out.println("Pre folder->>:" + folder);
		return ScanResult.continueScan;
	}
	
	public ScanResult postHandleFolder(File folder, ContextScan ctx) {
		System.out.println("Post folder<<-:" + folder);
		return ScanResult.continueScan;
	}

	@Override
	public ScanResult startScan(ContextScan ctx) throws Exception {
		System.out.println("Start Scan >>");
		return ScanResult.continueScan;
	}

	@Override
	public ScanResult endScan(ContextScan ctx) throws Exception {
		System.out.println("End Scan <<");
		return ScanResult.continueScan;
	}
}
