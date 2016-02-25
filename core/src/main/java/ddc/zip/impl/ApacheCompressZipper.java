package ddc.zip.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.IOUtils;

import ddc.zip.ILiteZipper;
import ddc.zip.LiteZipperException;

public class ApacheCompressZipper implements ILiteZipper {

	@Override
	public void unzip(Path source, Path dest) throws LiteZipperException {
		try {
			doUnzip(source, dest);
		} catch (ArchiveException | IOException e) {
			throw new LiteZipperException(e);
		}
	}

	private void doUnzip(Path source, Path dest) throws ArchiveException, IOException, LiteZipperException {
		InputStream is = null;
		OutputStream out = null;
		ArchiveInputStream in = null;
		try {
			is = new FileInputStream(source.toString());
			in = new ArchiveStreamFactory().createArchiveInputStream("zip", is);
			ZipArchiveEntry entry = (ZipArchiveEntry) in.getNextEntry();
			if (entry == null) {
				throw new LiteZipperException("First zip entry not found - file:[" + source + "]");
			}
			out = new FileOutputStream(new File(dest.toString(), entry.getName()));
			IOUtils.copy(in, out);
		} finally {
			if (out != null)
				out.close();
			if (is != null)
				is.close();
			if (in != null)
				in.close();
		}
	}

	@Override
	public void ungzip(Path source, Path dest) throws LiteZipperException {
		GZipper z = new GZipper();
		z.ungzip(source, dest);		
	}
	

	
}
