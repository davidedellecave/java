package ddc.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

public class FileUtil {
	public static String loadContent(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
	}
	
	public static String loadContent(Path path) throws IOException {
		return new String(Files.readAllBytes(path), "UTF-8");
	}
	
	public static Path replaceFilename(Path path, String filename) {
		return path.resolveSibling(Paths.get(filename));
	}
	
	public static Path prefixFileName(Path path, String prefix) {
		String newFilename = prefix + path.getFileName();		
		return replaceFilename(path, newFilename);
	}
	
	public static Path getPath(Path path, Path appendPath) {
		return path.resolve(appendPath.toString());
	}

	public static Path getPath(Path path, String appendPath) {
		return path.resolve(appendPath);
	}
	
	public static Path postfixFileName(Path path, String postfix) {
		String newFilename = path.getFileName() + postfix;		
		return replaceFilename(path, newFilename);
	}
    
	public static boolean areEqualSizeAndModifiedTime(Path f1, Path f2) throws IOException {
		long size1 = Files.size(f1);
		long size2 = Files.size(f2);
		FileTime time1 = Files.getLastModifiedTime(f1);
		FileTime time2 = Files.getLastModifiedTime(f2);
		return (size1==size2 && time1.toMillis()==time2.toMillis());	
	}

	public static boolean areEqualModifiedTime(Path f1, Path f2) throws IOException {
		FileTime time1 = Files.getLastModifiedTime(f1);
		FileTime time2 = Files.getLastModifiedTime(f2);
		return (time1.toMillis()==time2.toMillis());	
	}
	
	/**
	 * Rename file on filesystem
	 * @param path
	 * @param newName
	 * @throws IOException
	 */
	public static void rename(Path path, String newName) throws IOException {
		Files.move(path, path.resolveSibling(newName));
	}

	public static boolean isOlderThan(File file, long duration) {
		long date = System.currentTimeMillis()-duration;
		return (file.lastModified()<date);
	}
	
	public static boolean isNewerThan(File file, long duration) {
		long date = System.currentTimeMillis()-duration;
		return (file.lastModified()>=date);
	}
}
