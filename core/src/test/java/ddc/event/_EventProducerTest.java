package ddc.event;

import static org.junit.Assert.*;

import org.junit.Test;

import ddc.util.Chronometer;
import ddc.util.StoppableThread;

public class _EventProducerTest {

	@Test
	public <T> void testEventPipeManager() {
		EventPipeManager<String> t = new EventPipeManager<>(new EventProducer<String>() {
			@Override
			public void run() {
				int counter=0;
				while (counter<5 || this.isStopRequested()) {
					counter++;
					Chronometer.sleep(1000);
					produce(String.valueOf(counter));
				}
			}
		}, new EventConsumerListener<String>() {
			@Override
			public void consume(String event) throws Exception {
				System.out.println(event);
			}
		});

		t.start();
//		Chronometer.sleep(6 * 1000);
		t.waitAndStop();
//		t.stopAndWait();
		System.out.println("main terminated");
	}

}
