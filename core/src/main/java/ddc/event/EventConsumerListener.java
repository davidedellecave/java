package ddc.event;

public interface EventConsumerListener<T> {
	public void consume(T event) throws Exception;
}
