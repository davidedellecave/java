package ddc.event;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class QueueEventStream extends InputStream {
	private static final int WAIT_TO_POOL_MILLIS=200;
	private boolean inputIsStopped = false;
	private int lastChar = 0;
	private ByteArrayInputStream stream = null;
	private BlockingDeque<DataEvent> queue = null;
	private QueueEventListener listener = null;
	
	public QueueEventStream() {
		queue = new LinkedBlockingDeque<>();
		if (listener!=null) listener.onStart(queue.size());
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		if (listener!=null) listener.onClose(queue.size());
	}
	
	public void put(String id, byte[] data) throws InterruptedException {
//		System.out.println("Put requested...");
		if (inputIsStopped) {
			throw new InterruptedException("Cannot put new event because - input events are stopped:[" + inputIsStopped + "]");
		}
		DataEvent e = new DataEvent(id, data);
		queue.put(e);
		if (listener!=null) listener.onPut(e, queue.size());
	}

	public int size() {
		return queue.size();
	}
	
	
	public void stopInputEvents() {
		inputIsStopped=true;
		if (listener!=null) listener.onStopInput(queue.size());
	}
	
	private DataEvent pool() {
		DataEvent event = null;
		while (event == null) {
			try {
				event  = queue.poll(WAIT_TO_POOL_MILLIS, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				return null;
			}
			if (event!=null) {				
				return event;
			}
			if (event==null && inputIsStopped) return null;
		} 
		return null;
	}

	@Override
	public int read() throws IOException {
//		System.out.println("Read requested...");
		if (stream == null) {
			DataEvent event = pool();
			if (event!=null) {
				if (listener!=null) listener.onPool(event, queue.size());
				stream = new ByteArrayInputStream(event.getData());
			}
		}
		if (stream == null) {
			lastChar = -1;
		} else {
			lastChar = stream.read();
			if (lastChar==-1) stream = null;
		}
//		System.out.println(lastChar);
		return lastChar;
	}

	public QueueEventListener getListener() {
		return listener;
	}

	public void setListener(QueueEventListener listener) {
		this.listener = listener;
	}
}