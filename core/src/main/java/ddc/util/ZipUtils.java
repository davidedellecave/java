/*
 * Created on 21-apr-2005
 * @author davidedc
 */
package ddc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.collections4.EnumerationUtils;
import org.apache.commons.io.IOUtils;

/**
 * @author davided
 */
public class ZipUtils {

	/**
	 * Unzip a zip file to destination folder 
	 * @param zipName Source file
	 * @param destinationPath destination path
	 * @throws IOException
	 */
	public static void unzip(String zipName, String destinationPath)	throws IOException {
		unzip(new File(zipName), destinationPath);
	}

	/**
	 * Unzip a zip file to destination folder
	 * If destination folder hierarchy must exists
	 * @return list of destination filename
	 * @param zipFile Source file
	 * @param destinationPath Destination path
	 * @throws IOException
	 */
	public static List<File> unzip(File zipFile, String destinationPath) throws IOException {
		FileOutputStream out = null;
		LinkedList<File> unzipList = new LinkedList<File>();
		ZipFile zf = new ZipFile(zipFile);
		Enumeration<? extends ZipEntry> enum1 = zf.entries();
		try {
			while (enum1.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) enum1.nextElement();
				if (!entry.isDirectory()) { 
					//save entry
					File unzippedFile = new File(destinationPath, entry.getName());
					out = new FileOutputStream(unzippedFile );
					IOUtils.copy(zf.getInputStream(entry), out);
					unzipList.add(unzippedFile);
				}
			}
		} finally {
			if (out!=null) out.close();
			if (zf!=null) zf.close();
		}
		return unzipList; 
	}
	
	public static void zip(List<File> sourceList, File zipFile) throws FileNotFoundException, IOException {
		FileInputStream inputStream=null;
		ZipOutputStream outZip = null;
		try {
			outZip = new ZipOutputStream(new FileOutputStream(zipFile));
			Iterator<File> iter = sourceList.iterator();
			while (iter.hasNext()) {
				File source = iter.next();
				inputStream = new FileInputStream(source);
				ZipEntry entry = new ZipEntry(source.getName());
				outZip.putNextEntry(entry);			
				IOUtils.copy(inputStream, outZip);
				inputStream.close();
			}
		} finally {
			if (inputStream!= null) inputStream.close();
			if (outZip!=null) outZip.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<ZipEntry> getFileList(File zipFile) {        		
		ZipFile zf=null;
        try {
            zf = new ZipFile(zipFile);            
            Enumeration<? extends ZipEntry> zipEntries = zf.entries();
            return (List<ZipEntry>)EnumerationUtils.toList(zipEntries);            
        } catch (IOException e) {
        	System.err.println("getFileList() Exception:[" + e.getMessage() + "]");
	} finally {
		if (zf!=null)
			try {zf.close();} catch (IOException e) {}
	}
        return null;
    }
}
