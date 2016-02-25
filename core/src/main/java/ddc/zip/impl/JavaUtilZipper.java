package ddc.zip.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.collections.EnumerationUtils;
import org.apache.commons.io.IOUtils;

import ddc.zip.ILiteZipper;
import ddc.zip.LiteZipperException;

public class JavaUtilZipper implements ILiteZipper {

	@Override
	public void unzip(Path source, Path dest) throws LiteZipperException {
		try {
			doUnzip(source, dest);
		} catch (IOException e) {
			throw new LiteZipperException(e);
		}
	}

	public static void doUnzip(Path source, Path dest) throws IOException {
		FileOutputStream out = null;
		ZipFile zf = null;
		try {
			zf = new ZipFile(source.toString());
			Enumeration<? extends ZipEntry> enum1 = zf.entries();
			while (enum1.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) enum1.nextElement();
				if (!entry.isDirectory()) {
					// save entry
					File unzippedFile = new File(dest.toString(), entry.getName());
					out = new FileOutputStream(unzippedFile);
					IOUtils.copy(zf.getInputStream(entry), out);
				} else {

				}
			}
		} finally {
			if (out != null)
				out.close();
			if (zf != null)
				zf.close();
		}
	}

	private void zip(List<File> sourceList, File zipFile) throws FileNotFoundException, IOException {
		FileInputStream inputStream = null;
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
			if (inputStream != null)
				inputStream.close();
			if (outZip != null)
				outZip.close();
		}
	}

	@SuppressWarnings("unchecked")
	private List<ZipEntry> listFile(File zipFile) throws ZipException, IOException {
		ZipFile zf = null;
		try {
			zf = new ZipFile(zipFile);
			Enumeration<? extends ZipEntry> zipEntries = zf.entries();
			return (List<ZipEntry>) EnumerationUtils.toList(zipEntries);
		} finally {
			if (zf != null)
				try {
					zf.close();
				} catch (IOException e) {}
		}
	}

	@Override
	public void ungzip(Path source, Path dest) throws LiteZipperException {
		GZipper z = new GZipper();
		z.ungzip(source, dest);		
	}
}
