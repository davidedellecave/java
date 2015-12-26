/*
 * Created on 18-mar-2004
 * @author davidedc
 */

package ddc.console;

public abstract class AbstractConsoleApp implements Runnable {
	private String[] args;

	public AbstractConsoleApp() { }

	public abstract void infoApplicationHeader();
	public abstract void infoApplicationFooter();
	public abstract boolean validateArgs();
	public abstract void errorApplicationUsage();
	public abstract void execute() throws Exception;

	public void run() {
		if (getArgs()==null) throw new RuntimeException("Set args before calling run()");
		infoApplicationHeader();
		if (validateArgs()) {
			try {
				execute();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
		else errorApplicationUsage();
		infoApplicationFooter();
	}

	public String[] getArgs() {
		return args;
	}
	public void setArgs(String[] args) {
		this.args = args;
	}
}
