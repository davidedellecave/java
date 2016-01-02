package ddc.util;

import java.io.BufferedReader;

/*
 * Created on 01-Mar-2003 By davidedc
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;

public class FileUtils extends org.apache.commons.io.FileUtils {
	
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
	
	public static File getDailyRollerFilename(File file) {		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String date = "_" + formatter.format(new Date());
		return postfixFileName(file.toPath(), date).toFile();
	}
	
	public static boolean isReadbleFolder(String folder) {
		return isReadbleFolder(new File(folder));
	}
	
	public static boolean isReadbleFolder(File folder) {
		if (folder==null) return false;
		if (!folder.exists() || !folder.canRead()) return false;
		return true;		
	}
	
	public static boolean createWritebleFolder(String folder) {
		return createWritebleFolder(new File(folder));
	}
	public static boolean createWritebleFolder(File folder) {
		if (folder==null) return false;
		if (!folder.exists()) {
			if (!folder.mkdirs()) return false;
		} else {
			if (!folder.canWrite()) return false;
		}
		return true;
	}
	
//	public static FileName buildFileName(String path) {
//		FileName fn = new FileName();
//		String p = FilenameUtils.getFullPathNoEndSeparator(path);
//		fn.path = FilenameUtils.getPath(p);
//		fn.name = FilenameUtils.getName(p);
//		fn.ext = FilenameUtils.getExtension(p);
//		return fn;
//	}
	
	public static String getFileExtension(File file) {
		return getFileExtension(file.getName());
	}
	
	public static File renameFileDailyRoller(File file) {		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String date = "_" + formatter.format(new Date());
		return renameFileAddSuffix(file, date);
	}
	
	public static File renameFileTimestampRoller(File file) {		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String date = "_" + formatter.format(new Date());
		return renameFileAddSuffix(file, date);
	}
	
	public static File renameFileIdRoller(File file) {
		String id = "_" + UUID.randomUUID().toString();
		return renameFileAddSuffix(file, id);
	}

	public static File renameFileAddSuffix(File file, String suffix) {		
		String newName = getFilenameWhithoutExtension(file.getName()) + suffix;
		return FileUtils.renameFile(file, newName, true);
	}	

	public static FileName buildFileName(String path) {
		FileName fn = new FileName();
		String p = FilenameUtils.getFullPathNoEndSeparator(path);
		fn.path = FilenameUtils.getPath(p);
		fn.name = FilenameUtils.getName(p);
		fn.ext = FilenameUtils.getExtension(p);
		return fn;
	}
	
	
	/**
	 * Add a progressive index to make the filename unique
	 * Example. doc.xml -> doc-1.xml 
	 * @param file
	 * @return
	 */
	public static File getUniqueFileName(File file) {
		int counter=0;
		while (file.exists()) {
			counter++;
			file = FileUtils.renameFileAddSuffix(file, "-" + String.valueOf(counter));
		}
		return file;
	}
	
	public static String getFileExtension(String name) {
		return FilenameUtils.getExtension(name);
	}

	public static String getFilenameWhithoutExtension(String name) {
		return FilenameUtils.removeExtension(name);
	}
	
	public static String getFilenameWhithoutExtension(File file) {
		return getFilenameWhithoutExtension(file.getName());
	}
	
	public static File renameFile(File file, String newName, boolean preserveExtension) {
		String ext = getFileExtension(file);
		String path = file.getParent();
		if (preserveExtension) {
			return new File(path, newName + "." + ext);
		}
		return new File(path, newName);
	}
	
	public static File renameFileExtension(File file, String extension) {
		String name = getFilenameWhithoutExtension(file);
		String path = file.getParent();
		if (extension.length()>0) {
			return new File(path, name + "." + extension);			
		} else {
			return new File(path, name);
		}
	}

	public static File renameFileExtension(String filename, String extension) {
		return renameFileExtension(new File(filename), extension);
	}

	public static String getRootFolder(String path) {
		int pos = path.indexOf(File.separator);
		if (pos>=0) return path.substring(0, pos);  		
		return path;
	}

	public static void saveObject(String filename, Serializable object) throws IOException  {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
		       fos = new FileOutputStream(filename);
		       out = new ObjectOutputStream(fos);
		       out.writeObject(object);
		} finally {
			out.close();
		}
	}
	public static Object loadObject(String filename) throws IOException, ClassNotFoundException  {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		Object obj = null;
		try {
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			obj = in.readObject();
		} finally {
			in.close();
		}
		return obj;
	}
	
	/**
	 * Remove path separator from end string
	 * @param path
	 * @return
	 */
	public static String normalizePathSeparator(String path) {
		if (path==null) return "";
		if (path.endsWith(File.separator)) path=path.substring(0, path.lastIndexOf(File.separator));
		if (path.startsWith(File.separator)) path=path.substring(1);
		return path;	
	}
	
	public static String appendPathSeparator(String path) {
		if (path==null) return File.separator;
		if (!path.endsWith(File.separator)) path += File.separator;
		return path;
	}	
	
	public static String appendSeparator(String path, String separator) {
		if (path==null) return separator;
		if (!path.endsWith(separator)) path += separator;
		return path;
	}	
	
	public static String removeEndSeparator(String path) {
		return FilenameUtils.getFullPathNoEndSeparator(path);
	}
	
//	String path = FileUtils.relocatePath(source.getAbsolutePath(), sourceFolder, targetFolder);
	public static String relocatePath(String path, String basePath, String targetBasePath) {
		path=normalizePathSeparator(path);
		basePath=normalizePathSeparator(basePath);
		if (path.startsWith(basePath)) path = normalizePathSeparator(path.substring(basePath.length())); 
		path = path.replace(":", "");
		return appendPathSeparator(targetBasePath) + path;
	}

//	public static String byteCountToDisplaySize(long size) {
//		
//		return String.format("%.2f", f);
//		
//	}
	
    public static String formatBytes(long size) {
        String displaySize;

        if (size / ONE_GB > 0) {
            displaySize = String.format("%.2f", ((float)size) / ONE_GB) + " GB";
        } else if (size / ONE_MB > 0) {
            displaySize = String.format("%.2f", ((float)size) / ONE_MB) + " MB";
        } else if (size / ONE_KB > 0) {
        	displaySize = String.format("%.2f", ((float)size) / ONE_KB) + " KB";
        } else {
            displaySize = String.valueOf(size) + " bytes";
        }
        return displaySize;
    }
    
	public static String getJarFolder(Object packageClass) {
		URL url = packageClass.getClass().getProtectionDomain().getCodeSource().getLocation();
		File jarFile = new File(url.getPath());
		return jarFile.getParent();
	}
	
	//new Properties().load(Foo.class.getResourceAsStream("file.properties"))
	public static Properties loadProperties(File file) throws FileNotFoundException, IOException {
		// Read properties file.
		Properties properties = new Properties();
		properties.load(new FileInputStream(file));
		return properties;
	}

	public static String readFileAsString(File file) throws IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
	
    /**
     * Ritorna l'oggetto File per un file con il nome specificato, che si trovi nella cartella dove
     * si trova il file .class della classe specificata.
     * (nel caso di file jar, nella directory del jar).
     * 
     * NB sotto un Servlet Container (Tomcat) potrebbe non funzionare come ci si aspetta
     * @param clazz
     * @param filename
     * @return
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     */
	public static File getLocalFile(Class<? extends Object> clazz, String filename) throws URISyntaxException, UnsupportedEncodingException {
		CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
		String location = codeSource.getLocation().toString();
		String urlEncoded = location.replaceAll(" ", "%20");
		URI uri = new URI(urlEncoded);
		File jarFile = new File(uri.getPath());
		File jarDir = jarFile.getParentFile();
		File propFile = null;
		if (jarDir != null && jarDir.isDirectory()) { 
			propFile = new File(jarDir, filename); 
		}
		return propFile;
	}
}