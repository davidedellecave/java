package ddc.event;

import java.util.concurrent.BlockingDeque;

public class _EventConsumer<T> implements Runnable{
	private BlockingDeque<T> queue = null;
		
	public _EventConsumer(BlockingDeque<T>  queue) {
		super();
		this.queue = queue;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
