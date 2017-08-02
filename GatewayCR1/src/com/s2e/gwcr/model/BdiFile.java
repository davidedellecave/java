package com.s2e.gwcr.model;

public class BdiFile {
	private String fileName;
	private String path;
	private long lastModifiedTime;
	private long size;
	private boolean directory;
	private boolean regularFile;
	private boolean symbolicLink;
	private boolean other;
	private int permissions;
	

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getLastModifiedTime() {
		return lastModifiedTime;
	}
	public void setLastModifiedTime(long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public boolean isDirectory() {
		return directory;
	}
	public void setIsDirectory(boolean directory) {
		this.directory = directory;
	}
	public boolean isRegularFile() {
		return regularFile;
	}
	public void setIsRegularFile(boolean regularFile) {
		this.regularFile = regularFile;
	}
	public boolean isSymbolicLink() {
		return symbolicLink;
	}
	public void setIsSymbolicLink(boolean symbolicLink) {
		this.symbolicLink = symbolicLink;
	}
	public boolean isOther() {
		return other;
	}
	public void setIsOther(boolean other) {
		this.other = other;
	}
	public int getPermissions() {
		return permissions;
	}
	public void setPermissions(int permissions) {
		this.permissions = permissions;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}



//{
//	"files": [
//		{
//			"fileName": "com120_99999_20170613172957.zip.p7e.p7m",
//			"lastModifiedTime": 1480080720000,
//			"size": 87613,
//			"isDirectory": false,
//			"isRegularFile": true,
//			"isSymbolicLink": false,
//			"isOther": false,
//			"permissions": "644"
//		},
//		{
//			"fileName": "com910_A2A-84627738_20170613180606.pdf",
//			"lastModifiedTime": 1480331460000,
//			"size": 91444,
//			"isDirectory": false,
//			"isRegularFile": true,
//			"isSymbolicLink": false,
//			"isOther": false,
//			"permissions": "644"
//		}
//	]
//}