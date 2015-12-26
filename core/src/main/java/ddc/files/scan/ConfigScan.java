package ddc.files.scan;

import java.io.File;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class ConfigScan {
	private File rootFolder = null;
	private boolean isRecursive = true;
	private long sleepMillis = 10;
	private IOFileFilter dirFilter = TrueFileFilter.TRUE;
	private IOFileFilter fileFilter = TrueFileFilter.TRUE;

	public File getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(File rootFolder) {
		this.rootFolder = rootFolder;
	}

	public boolean isRecursive() {
		return isRecursive;
	}

	public void setRecursive(boolean isRecursive) {
		this.isRecursive = isRecursive;
	}

	public long getSleepMillis() {
		return sleepMillis;
	}

	public void setSleepMillis(long sleepMillis) {
		this.sleepMillis = sleepMillis;
	}

	public IOFileFilter getDirFilter() {
		return dirFilter;
	}

	public void setDirFilter(IOFileFilter dirFilter) {
		this.dirFilter = dirFilter;
	}

	public IOFileFilter getFileFilter() {
		return fileFilter;
	}

	public void setFileFilter(IOFileFilter fileFilter) {
		this.fileFilter = fileFilter;
	}

}
