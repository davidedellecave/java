package ddc.files.scan;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ddc.files.scan.ScanFolder.ScanResult;

public class ScanUtil {
	
	public static List<Path> getFiles(Path folder, String[] extension) throws Exception {
		if (!folder.toFile().isDirectory()) {
			return Collections.emptyList();
		}
		ScanFolder s = new ScanFolder();
		final List<Path> list = new ArrayList<>();
		s.deepFirstScan(folder.toFile(), true, new BaseFolderHandler() {
			@Override
			public ScanResult handleFile(File file, ContextScan ctx) {
				for (String ext: extension) {
					if (file.getName().endsWith(ext)) list.add(file.toPath());					
				}
				return ScanResult.continueScan;
			}
		});
		return list;
	}
	
	
	public static List<Path> getFiles(Path folder) throws Exception {
		if (!folder.toFile().isDirectory()) {
			return Collections.emptyList();
		}
		ScanFolder s = new ScanFolder();
		final List<Path> list = new ArrayList<>();
		s.deepFirstScan(folder.toFile(), true, new BaseFolderHandler() {
			@Override
			public ScanResult handleFile(File file, ContextScan ctx) {				
				list.add(file.toPath());
				return ScanResult.continueScan;
			}
		});
		return list;
	}
	
	public static List<Path> getFolders(Path folder) throws Exception {
		if (Files.exists(folder)) {
			return Collections.emptyList();
		}
		ScanFolder s = new ScanFolder();
		final List<Path> list = new ArrayList<>();
		s.deepFirstScan(folder.toFile(), true, new BaseFolderHandler() {
			@Override
			public ScanResult preHandleFolder(File file, ContextScan ctx) {	
				list.add(file.toPath());
				return ScanResult.continueScan;
			}
		});
		return list;
	}
}
