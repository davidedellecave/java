package ddc.zip;

import java.nio.file.Path;

public interface ILiteZipper {
	public void unzip(Path source, Path dest) throws LiteZipperException;
	public void ungzip(Path source, Path dest) throws LiteZipperException;
}
