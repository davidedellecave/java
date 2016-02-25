package ddc.util;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.APPEND;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextFile {
	private Path path = null;
	private String charset ="UTF-8";
	private String text = "";

	public TextFile(Path path) {
		this.path=path;
	}
	
	public TextFile(Path path, String charset) {
		this.path=path;
		this.charset=charset;
	}
	
	public void load() throws UnsupportedEncodingException, IOException {
		text = new String(Files.readAllBytes(path), charset);
	}
	
	public void create() throws UnsupportedEncodingException, IOException {
		create(path, text);
	}
	public void append() throws UnsupportedEncodingException, IOException {
		append(path, text);
	}
	
	public String getFilename() {
		return path.getFileName().toString();
	}
	
	public static void create(Path path, String text) throws UnsupportedEncodingException, IOException {
		Files.write(path, text.getBytes(), CREATE);
	}
	
	public static void append(Path path, String text) throws UnsupportedEncodingException, IOException {
		Files.write(path, text.getBytes(), APPEND);
	}

	//

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
