package ddc.event;

import java.util.ArrayList;
import java.util.List;

public class ConsumerListenerList<T> implements ConsumerListener<T> {
	private List<ConsumerListener<T>> list = new  ArrayList<>();

	@Override
	public void consume(T event) throws Exception {
		for (ConsumerListener<T> listener : list) {
			listener.consume(event);
		}
	}
	
	public void addListener(ConsumerListener<T> listener) {
		list.add(listener);
	}
	
	public void removeListener(ConsumerListener<T> listener) {
		list.remove(listener);
	}

	public void removeListener(int index) {
		list.remove(index);
	}

}
