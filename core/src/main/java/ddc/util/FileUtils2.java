package ddc.util;

/*
 * Created on 01-Mar-2003 By davidedc
 */

import java.io.BufferedReader;
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
import java.security.CodeSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class FileUtils2  {
//	private static SimpleDateFormat dateFormatterForFile = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	
	public static String getFileExtension(File file) {
		return getFileExtension(file.getName());
	}
	
	public static File getDailyRollerFilename(File file) {		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String date = "_" + formatter.format(new Date());
		return addSuffixToFilename(file, date);
	}
	
	public static File addSuffixToFilename(File file, String suffix) {		
		String newName = getFilenameWhithoutExtension(file.getName()) + suffix;
		return FileUtils2.renameFile(file, newName, true);
	}
	
	public static String getFileExtension(String name) {
		String rv = "";
		int p = name.lastIndexOf('.');
		if (p != -1) {
			rv = name.substring(p + 1).toLowerCase();
		}
		return rv;
	}

	public static String getFilenameWhithoutExtension(String name) {
		String prefix = name;
		int p = name.lastIndexOf('.');
		if (p != -1) {
			prefix = name.substring(0, p).toLowerCase();
		}
		return prefix;
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
		return new File(path, name + "." + extension);
	}

	public static File renameFileExtension(String filename, String extension) {
		return renameFileExtension(new File(filename), extension);
	}

	public static String getRootFolder(String path) {
		int pos = path.indexOf(File.separator);
		if (pos>=0) return path.substring(0, pos); 
		if (StringUtils.isBlank(path)) path = "root";
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

        if (size / org.apache.commons.io.FileUtils.ONE_GB > 0) {
            displaySize = String.format("%.2f", ((float)size) / org.apache.commons.io.FileUtils.ONE_GB) + " GB";
        } else if (size / org.apache.commons.io.FileUtils.ONE_MB > 0) {
            displaySize = String.format("%.2f", ((float)size) / org.apache.commons.io.FileUtils.ONE_MB) + " MB";
        } else if (size / org.apache.commons.io.FileUtils .ONE_KB > 0) {
        	displaySize = String.format("%.2f", ((float)size) / org.apache.commons.io.FileUtils .ONE_KB) + " KB";
        } else {
            displaySize = String.valueOf(size) + " bytes";
        }
        return displaySize;
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
	
	public static String getJarFolder(Object packageClass) {
		URL url = packageClass.getClass().getProtectionDomain().getCodeSource().getLocation();
		File jarFile = new File(url.getPath());
		return jarFile.getParent();
	}
	
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

}