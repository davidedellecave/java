package ddc.files.scan;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ddc.files.scan.ScanFolder.ScanResult;
import ddc.util.FileUtil;

public class ScanUtil {

	public static List<Path> getFiles(Path folder, boolean recursive, final String[] includeExtension, final String[] excludeExtension) throws Exception {
		return getFiles(folder, recursive, -1, includeExtension, excludeExtension);
	}

	public static List<Path> getFiles(Path folder, boolean recursive, long olderThanMillis, final String[] includeExtension, final String[] excludeExtension)
			throws Exception {
		if (!folder.toFile().isDirectory()) {
			return Collections.emptyList();
		}
		
		ScanFolder s = new ScanFolder();
		final List<Path> list = new ArrayList<>();
		s.deepFirstScan(folder.toFile(), recursive, new BaseFolderHandler() {
			@Override
			public ScanResult handleFile(File file, ContextScan ctx) {
				boolean toAdd = false;
				
				if (olderThanMillis>0 && FileUtil.isNewerThan(file, olderThanMillis)) {
					return ScanResult.continueScan;
				}
				
				if (includeExtension.length > 0) {
					for (String ext : includeExtension) {
						if (file.getName().endsWith(ext))
							toAdd = true;
					}				
				} else {
					toAdd=true;
				}
				
				if (excludeExtension.length > 0) {
					for (String ext : excludeExtension) {
						if (file.getName().endsWith(ext))
							toAdd = false;
					}				
				}

				if (toAdd)
					list.add(file.toPath());
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
