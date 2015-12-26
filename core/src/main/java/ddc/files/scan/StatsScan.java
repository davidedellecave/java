package ddc.files.scan;

import ddc.util.Chronometer;

public class StatsScan {
	private Chronometer chron = new Chronometer();
	private long fileScanned = 0;
	private long fileProcessed = 0;
	private long bytesProcessed = 0;
	private long folderScanned = 0;
	private long folderProcessed = 0;
	private String throughputSize = "0/sec";
	private String sizeProcessed = "0";
	private float throughput = 0;
	private int currentDeepLevel = 0;

	public Chronometer getChron() {
		return chron;
	}

	public void setChron(Chronometer chron) {
		this.chron = chron;
	}

	public long getFileScanned() {
		return fileScanned;
	}

	public void setFileScanned(long fileScanned) {
		this.fileScanned = fileScanned;
	}

	public long getFileFiltered() {
		return fileProcessed;
	}

	public void setFileFiltered(long fileFiltered) {
		this.fileProcessed = fileFiltered;
	}

	public long getBytesProcessed() {
		return bytesProcessed;
	}

	public void setBytesProcessed(long bytesProcessed) {
		this.bytesProcessed = bytesProcessed;
	}

	public long getFolderScanned() {
		return folderScanned;
	}

	public void setFolderScanned(long folderScanned) {
		this.folderScanned = folderScanned;
	}

	public long getFolderFiltered() {
		return folderProcessed;
	}

	public void setFolderFiltered(long folderFiltered) {
		this.folderProcessed = folderFiltered;
	}

	public String getThroughputSize() {
		return throughputSize;
	}

	public void setThroughputSize(String throughputSize) {
		this.throughputSize = throughputSize;
	}

	public String getSizeProcessed() {
		return sizeProcessed;
	}

	public void setSizeProcessed(String sizeProcessed) {
		this.sizeProcessed = sizeProcessed;
	}

	public float getThroughput() {
		return throughput;
	}

	public void setThroughput(float throughput) {
		this.throughput = throughput;
	}

	public int getCurrentDeepLevel() {
		return currentDeepLevel;
	}

	public void incCurrentDeepLevel() {
		this.currentDeepLevel++;
	}

	public void decCurrentDeepLevel() {
		this.currentDeepLevel--;
	}

	public void setCurrentDeepLevel(int currentDeepLevel) {
		this.currentDeepLevel = currentDeepLevel;
	}

	public void incFolderScanned() {
		this.folderScanned++;
	}

	public void incFolderProcessed() {
		this.folderProcessed++;
	}

	public void incFileScanned() {
		this.fileScanned++;
	}

	public void incFileProcessed() {
		this.fileProcessed++;
	}

	public void incBytesProcessed(long length) {
		this.bytesProcessed += length;
	}

	public long getFileProcessed() {
		return fileProcessed;
	}

	public void setFileProcessed(long fileProcessed) {
		this.fileProcessed = fileProcessed;
	}

	public long getFolderProcessed() {
		return folderProcessed;
	}

	public void setFolderProcessed(long folderProcessed) {
		this.folderProcessed = folderProcessed;
	}
	

}
