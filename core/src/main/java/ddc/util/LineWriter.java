package ddc.util;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.io.IOUtils;

public class LineWriter {
	public final static String NEW_LINE = System.getProperty("line.separator");
	private Writer writer;	
	private String header;
	private String footer;
	
	/**
	 * Write an item as string
	 */
	public void write(Object item) throws IOException {
		if (item==null ) return;		
		String s = String.valueOf(item);
		writer.write(s + NEW_LINE);
	}

	public void close()  {
		if (getFooter()!=null) {
			try {
				write(getFooter());
			} catch (Exception e) {
				//ignore
			}
		}
		IOUtils.closeQuietly(writer);
	}

	public void open() throws IOException {
		if (writer==null) throw new IOException("writer is null");
		if (getHeader()!=null) write(getHeader());
	}
	
	public void setWriter(Writer writer) {
		this.writer=writer;
	}
	public Writer getWriter() {
		return writer;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

}
