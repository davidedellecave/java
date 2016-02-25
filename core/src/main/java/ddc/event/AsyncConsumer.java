package ddc.event;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeUnit;

import ddc.util.Timespan;

public class AsyncConsumer<T> implements Runnable {
	private BlockingDeque<T> queue = null;
	private ConsumerListener<T> consumer = null;
	private Timespan waitforPoll = Timespan.createTimespan(10, TimeUnit.SECONDS);
	private boolean stopRequest = false;

	public AsyncConsumer(BlockingDeque<T> queue, ConsumerListener<T> consumer) {
		this.queue = queue;
		this.consumer = consumer;
	}

	public boolean isStopRequest() {
		return stopRequest;
	}

	public void stop() {
		this.stopRequest = true;
	}

	@Override
	public void run() {
		System.out.println("AsynchConsumer - Running...");
		while (!isStopRequest()) {
			try {
				T obj = queue.poll(waitforPoll.getMillis(), TimeUnit.MILLISECONDS);
				if (obj != null)
					consumer.consume(obj);
			} catch (Exception e) {
			}
		}
		System.out.println("AsynchConsumer - stop requested - queue size[:" + queue.size() + "]");
		while (queue.size() > 0) {
			try {
				T obj = queue.poll(waitforPoll.getMillis(), TimeUnit.MILLISECONDS);
				if (obj != null)
					consumer.consume(obj);
			} catch (Exception e) {
			}
		}
		System.out.println("AsynchConsumer - Terminated");
	}

}
