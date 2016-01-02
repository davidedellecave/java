package ddc.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class FileLineWriter extends LineWriter {
	private File file;
	private String fileEncoding=null;
	private int fileBufferSize=-1;	
	private boolean append=false;

	public FileLineWriter() {}
	
	public FileLineWriter(File file) {
		this.file=file;
	}
	
	public void open() throws IOException {  
		BufferedWriter writer;
		OutputStream out = new FileOutputStream(file, append);
		String path = file.getParent();
		File folder = new File(path);
		if (!folder.exists()) folder.mkdirs();
		if (fileBufferSize!=-1 && fileEncoding!=null) {
			writer = new BufferedWriter(new OutputStreamWriter(out, fileEncoding), fileBufferSize);
		} else if (fileBufferSize!=-1) {
			writer = new BufferedWriter(new OutputStreamWriter(out), fileBufferSize);
		} else {
			writer = new BufferedWriter(new OutputStreamWriter(out));
		}
		super.setWriter(writer);
		super.open();
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileEncoding() {
		return fileEncoding;
	}

	public void setFileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}

	public int getFileBufferSize() {
		return fileBufferSize;
	}

	public void setFileBufferSize(int fileBufferSize) {
		this.fileBufferSize = fileBufferSize;
	}

	public boolean isAppend() {
		return append;
	}

	public void setAppend(boolean append) {
		this.append = append;
	}
	
	
}
