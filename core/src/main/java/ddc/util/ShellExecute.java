package ddc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;

public class ShellExecute {

	private Process proc = null;
	private String shellCommand = null;
	private PrintWriter out = null;
	private BufferedReader in = null;

	// private ErrorReader errorReader = null;

	public ShellExecute(String shellCommand) {
		this.shellCommand = shellCommand;
	}

	public void createProcess() throws IOException {
		ProcessBuilder pb = new ProcessBuilder(shellCommand);
		pb.redirectOutput(Redirect.PIPE);
		proc = pb.start();
		in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(proc.getOutputStream()));
	}

	public void execute(String command) throws IOException, InterruptedException {
		if (proc == null)
			createProcess();
		out.println(command);		
		out.flush();
		proc.waitFor();
//		close();
//		proc=null;
		//String line;
		//while ((line=in.readLine())!=null) System.out.println(line);
	}

	public void close() {
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
}
