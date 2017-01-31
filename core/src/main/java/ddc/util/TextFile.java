package ddc.util;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.APPEND;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextFile {
	private Path path = null;
	private String charset ="UTF-8";
	private String text = "";

	public static void create(Path path, String text) throws UnsupportedEncodingException, IOException {
		Files.write(path, text.getBytes(), CREATE);
	}
	
	public static void create(String path, String text) throws UnsupportedEncodingException, IOException {
		Files.write(Paths.get(path), text.getBytes(), CREATE);
	}
	
	
	/**
	 * Append text to file, if file does not exist will be created
	 */
	public static void append(Path path, String text) throws UnsupportedEncodingException, IOException {
		if (!Files.exists(path)) {
			Files.write(path, text.getBytes(), CREATE);
		} else {
			Files.write(path, text.getBytes(), APPEND);	
		}
		
	}
	
	public static void append(String path, String text) throws UnsupportedEncodingException, IOException {
		append(Paths.get(path), text); 
	}

	public static String load(String path) throws UnsupportedEncodingException, IOException {
		return load(Paths.get(path));
	}

	public static String load(Path path) throws UnsupportedEncodingException, IOException {
		return new String(Files.readAllBytes(path), "UTF-8");
	}
	
	public static TextFile newLoadInstance(Path path) throws UnsupportedEncodingException, IOException {
		TextFile tf = new TextFile(path);
		tf.load();
		return tf;
	}
	
	public static TextFile newCreateInstance(Path path, String text) throws UnsupportedEncodingException, IOException {
		TextFile tf = new TextFile(path);
		tf.setText(text);
		tf.create();
		return tf;
	}
	
	public static TextFile newAppendInstance(Path path, String text) throws UnsupportedEncodingException, IOException {
		TextFile tf = new TextFile(path);
		tf.setText(text);
		tf.append();
		return tf;
	}
	
	public TextFile(Path path) {
		this.path=path;
	}
	
	public TextFile(String path) {
		this.path=Paths.get(path);
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
