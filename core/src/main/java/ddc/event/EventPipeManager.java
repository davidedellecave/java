package ddc.event;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import ddc.util.Timespan;

public class EventPipeManager<T> {
	private BlockingDeque<T> queue = null;
	private EventProducer<T> producer=null;
	private EventConsumerListener<T> consumerListener;
	private boolean stopRequest = false;
	private boolean startRequest = false;
	private Thread thProducer = null;
	private Thread thConsumer = null;
	
	public EventPipeManager(EventProducer<T> producer, EventConsumerListener<T> listener) {
		super();	
		this.producer=producer;
		this.consumerListener=listener;
	}

	public void start() {
		System.out.println("EventPipeManager - Starting...");
		if (startRequest) {
			throw new EventException("Event Manager is already started");
		}
		if (stopRequest) {
			throw new EventException("Event Manager is already stopped");
		}
		startRequest=true;
		
		queue = new LinkedBlockingDeque<>();
		EventConsumer<T> consumer = new EventConsumer<>();
		thConsumer = new Thread(consumer);
		thConsumer.start();
		
		producer.setManger(this);
		thProducer = new Thread(producer);
		thProducer.start();		
		
		final EventPipeManager<T> manager = this;
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {				
				if (!manager.isStopRequest()) {
					System.out.println("EventPipeManager - ShutdownHook stop requesting...");
					manager.stopAndWait();
				}
			}
		});
	}
	

	public void waitAndStop() {
		waitProducer();
		stop();
		waitConsumer();
	}

	public void stopAndWait() {
		stop();
		waitProducer();
		waitConsumer();
	}
	
	public boolean isStopRequest() {
		return stopRequest;
	}
	
	private void waitProducer() {
		try {
			System.out.println("EventPipeManager - Producer waiting...");
			thProducer.join();
			System.out.println("EventPipeManager - Producer terminated");
		} catch (InterruptedException e) {
			System.err.println("Event manager - Producer waiting - exception:[" + e.getMessage() + "]");	
		}		
	}

	private void waitConsumer() {
		try {
			System.out.println("EventPipeManager - Consumer waiting...");			
			thConsumer.join();
			System.out.println("EventPipeManager - Consumer terminated");
		} catch (InterruptedException e) {
			System.err.println("Event manager - Consumer waiting - exception:[" + e.getMessage() + "]");	
		}	
	}

	private void stop() {
		System.out.println("EventPipeManager - Stopping...");
		this.stopRequest = true;
	}

	
	
	public void produce(T t) {		
		if (!startRequest) {
			throw new EventException("Event Manager - produce - must be started before");
		}
		if (stopRequest) {
			throw new EventException("Event Manager - produce - is already stopped");
		}		
		queue.addLast(t);
	}

	
	private class EventConsumer<T2> implements Runnable {
		private Timespan waitforPoll = Timespan.createTimespan(1, TimeUnit.SECONDS);
		
		@Override
		public void run() {
			System.out.println("EventConsumer - Running...");
			while (!isStopRequest()) {
				try {
					T obj = queue.poll(waitforPoll.getMillis(), TimeUnit.MILLISECONDS);
					if (obj != null)
						consumerListener.consume(obj);
				} catch (Exception e) {
				}
			}
			System.out.println("EventConsumer - stop requested - queue size:[" + queue.size() + "]");
			while (queue.size() > 0) {
				try {
					T obj = queue.poll(waitforPoll.getMillis(), TimeUnit.MILLISECONDS);
					if (obj != null)
						consumerListener.consume(obj);
				} catch (Exception e) {
				}
			}
			System.out.println("EventConsumer - Terminated");			
		}
	}
}


