package ddc.event;

public interface ConsumerListener<T> {
	public void consume(T event) throws Exception;
}
