package ddc.event;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import ddc.util.Chronometer;
import ddc.util.Timespan;

public class AsyncConsumerTest {

	@Test
	public void testAsyncConsumer() throws InterruptedException {
		BlockingDeque<String> queue = new LinkedBlockingDeque<>();
		TestConsumer consumer = new TestConsumer();
		_AsyncConsumer<String> asynch = new _AsyncConsumer<>(queue, consumer);
		
		Thread t = new Thread(asynch);
		t.start();	
		
		for (int i=0; i<10; i++) {
			queue.put("item 1." + i);
			queue.put("item 2." + i);
			queue.put("item 3." + i);
			Chronometer.sleep(new Timespan(1, TimeUnit.SECONDS));
			if (Math.random()>0.8) {
				System.out.println("Stop requested...");	
				asynch.stop();
				break;
			}
		}		
		System.out.println("Waiting to close the thread...");
		t.join();
		System.out.println("Terminated");
	}

	
	class TestConsumer implements ConsumerListener<String> {

		@Override
		public void consume(String event) {
			System.out.println("Consume:" + event);			
		}		
	}
}
