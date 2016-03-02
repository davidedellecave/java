package ddc.event;

public abstract class EventProducer<T> implements Runnable {
	private EventPipeManager<T> manager = null;

	public void produce(T event) {
		manager.produce(event);
	}

	public boolean isStopRequested() {
		return manager.isStopRequest();
	}

	protected void setManger(EventPipeManager<T> manager) {
		this.manager=manager;
	}
}
