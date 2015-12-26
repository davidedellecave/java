package ddc.console;

import java.util.ArrayList;
import java.util.List;

public abstract class MultitaskBatchApp extends ConfigurableConsoleApp {
	private boolean multithread=false;
	private List<ConfigurableConsoleApp> apps=null;
		
	public boolean isMultithread() {
		return multithread;
	}

	public void setMultithread(boolean multithread) {
		this.multithread = multithread;
	}

	public boolean validateConfiguration(Configuration configuration) {
		if (!(configuration instanceof MultitaskConfiguration)) {			
			System.err.println("Configuration type mismatch, class:[" + configuration.getClass().getName() + " required class:[" + MultitaskConfiguration.class.getName() +"]");
			return false;
		}
		apps = new ArrayList<ConfigurableConsoleApp>();
		MultitaskConfiguration multiConfig = (MultitaskConfiguration) configuration;
		for (Configuration c : multiConfig.getConfigurations()) {
			ConfigurableConsoleApp app = getNewBatchApp();
			if (app.validateConfiguration(c)) {
				apps.add(app);
			}
		}
		return true;
	}
	
	public void execute() throws Exception {
		for (ConfigurableConsoleApp app : apps) {
			if (multithread) {
				Thread t = new Thread(new Executor(app));
				t.start();
			} else {
				app.execute();
			}
		}
	}
	
	private class Executor implements Runnable {
		private ConfigurableConsoleApp app = null;
		public Executor(ConfigurableConsoleApp app) {
			this.app=app;
		}
		public void run() {
			try {
				app.execute();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public abstract ConfigurableConsoleApp getNewBatchApp();

}
