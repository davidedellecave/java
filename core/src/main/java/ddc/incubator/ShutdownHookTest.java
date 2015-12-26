package ddc.incubator;

public class ShutdownHookTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread t = new Thread() {
			public void run() {
				System.out.println("Shoutting down");
			}
		};
		 Runtime.getRuntime().addShutdownHook(t);

	}
}
