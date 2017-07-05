package ddc.zip.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import ddc.zip.LiteZipperException;

public class GZipper {
//	InputStream fileStream = new FileInputStream(filename);
//	InputStream gzipStream = new GZIPInputStream(fileStream);
//	Reader decoder = new InputStreamReader(gzipStream, encoding);
//	BufferedReader buffered = new BufferedReader(decoder);
	public void ungzip(Path source, Path dest) throws LiteZipperException {
		try {
			doUngzip(source, dest);
		} catch (ArchiveException | IOException e) {
			throw new LiteZipperException(e);
		}
	}

	private void doUngzip(Path source, Path dest) throws ArchiveException, IOException, LiteZipperException {
		InputStream is = null;
		OutputStream out = null;
		GZIPInputStream in = null;
		try {			
			is = new FileInputStream(source.toString());
			in = new GZIPInputStream(is);
			String outFilename = StringUtils.removeEnd(source.getFileName().toString(), ".gz");
			out = new FileOutputStream(new File(dest.toString(), outFilename));
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
}
