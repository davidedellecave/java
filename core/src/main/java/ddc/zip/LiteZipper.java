package ddc.zip;

import java.nio.file.Path;
import java.nio.file.Paths;

import ddc.zip.impl.ApacheCompressZipper;

public class LiteZipper {
	private ILiteZipper impl = new ApacheCompressZipper();
//	private ILiteZipper impl = new JavaUtilZipper();

	public void unzip(Path source, Path dest) throws LiteZipperException {
		impl.unzip(source, dest);
	}
	
	public void ungzip(Path source, Path dest) throws LiteZipperException {
		impl.ungzip(source, dest);
	}
	
	public static void main(String[] args) throws LiteZipperException {
		LiteZipper z = new LiteZipper();

		Path source = Paths.get("/Users/dellecave/data/tvsc/log-in/tvsc.access.log-20151221.gz");
		Path dest = Paths.get("/Users/dellecave/data/tvsc/log-in");

		z.ungzip(source, dest);

	}
}
