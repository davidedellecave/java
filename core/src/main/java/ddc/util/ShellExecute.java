package ddc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;

public class ShellExecute {

	private Process proc = null;
	private StringBuffer output = new StringBuffer();
	private Throwable exception = null;

	public void executeAsyncAndClose(final String shellCommand) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					executeAndClose(shellCommand);
				} catch (Throwable e) {
					exception = e;
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
	}

	public String executeAndClose(final String shellCommand) throws IOException, InterruptedException {
		output.delete(0, output.length());
		try {
			if (proc == null)
				createProcess(shellCommand);
			proc.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
		} finally {
			close();
		}
		return output.toString();
	}

	private void createProcess(final String shellCommand) throws IOException {
		ProcessBuilder pb = new ProcessBuilder(shellCommand);
		pb.redirectOutput(Redirect.PIPE);
		proc = pb.start();
	}

	public StringBuffer getOutput() {
		return output;
	}

	public Throwable getException() {
		return exception;
	}

	private void close() {
		InputStream in = proc.getInputStream();
		OutputStream out = proc.getOutputStream();
		if (in != null)
			try {
				in.close();
				if (out != null)
					out.close();
				if (proc != null)
					proc.destroy();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	// private String executeCommand(String command) throws IOException,
	// InterruptedException {
	// StringBuffer output = new StringBuffer();
	//
	// Process p = null;
	// try {
	// p = Runtime.getRuntime().exec(command);
	// p.waitFor();
	// BufferedReader reader = new BufferedReader(new
	// InputStreamReader(p.getInputStream()));
	// String line = "";
	// while ((line = reader.readLine()) != null) {
	// output.append(line + "\n");
	// }
	// String out = output.toString();
	// return out;
	// } finally {
	// if (p != null)
	// p.destroy();
	// }
	// }
}
