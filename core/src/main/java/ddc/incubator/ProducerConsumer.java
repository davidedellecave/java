package ddc.incubator;

public class ProducerConsumer {

	public static void main(String[] args) {
		ProducerConsumer pc = new ProducerConsumer();
		ProducerConsumer.CubbyHole c = pc.new CubbyHole();
		ProducerConsumer.Producer p1 = pc.new Producer(c, 1);
		ProducerConsumer.Consumer c1 = pc.new Consumer(c, 1);

        p1.start();
        c1.start();
    }
	
	class CubbyHole {
	    private int contents;
	    private boolean available = false;
	    
		public synchronized void put(int value) {
		    while (available == true) {
		        try {
		            // wait for Consumer to get value
		            wait();
		        } catch (InterruptedException e) {
		        }
		    }
		    contents = value;
		    available = true;
		    // notify Consumer that value has been set
		    notifyAll();
		}
	
		public synchronized int get() {
		    while (available == false) {
		        try {
		            // wait for Producer to put value
		            wait();
		        } catch (InterruptedException e) {
		        }
		    }
		    available = false;
		    // notify Producer that value has been retrieved
		    notifyAll();
		    return contents;
		}
	}
	
	class Producer extends Thread {
	    private CubbyHole cubbyhole;
	    private int number;
	
	    public Producer(CubbyHole c, int number) {
	        cubbyhole = c;
	        this.number = number;
	    }
	
	    public void run() {
	        for (int i = 0; i < 10; i++) {
	            cubbyhole.put(i);
	            System.out.println("Producer #" + this.number + " put: " + i);
	            try {
	                sleep((int)(Math.random() * 100));
	            } catch (InterruptedException e) { }
	        }
	    }
	}

	class Consumer extends Thread {
	    private CubbyHole cubbyhole;
	    private int number;
	
	    public Consumer(CubbyHole c, int number) {
	        cubbyhole = c;
	        this.number = number;
	    }
	
	    public void run() {
	        int value = 0;
	        for (int i = 0; i < 10; i++) {
	            value = cubbyhole.get();
	            System.out.println("Consumer #" + this.number + " got: " + value);
	        }
	    }
	}
}
	