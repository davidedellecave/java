package ddc.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    
	/**
	 * Rename file on filesystem
	 * @param path
	 * @param newName
	 * @throws IOException
	 */
	public static void rename(Path path, String newName) throws IOException {
		Files.move(path, path.resolveSibling(newName));
	}
}
