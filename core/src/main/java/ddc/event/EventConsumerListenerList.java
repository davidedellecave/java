package ddc.event;

import java.util.ArrayList;
import java.util.List;

public class EventConsumerListenerList<T> implements EventConsumerListener<T> {
	private List<EventConsumerListener<T>> list = new  ArrayList<>();

	@Override
	public void consume(T event) throws Exception {
		for (EventConsumerListener<T> listener : list) {
			listener.consume(event);
		}
	}
	
	public void addListener(EventConsumerListener<T> listener) {
		list.add(listener);
	}
	
	public void removeListener(EventConsumerListener<T> listener) {
		list.remove(listener);
	}

	public void removeListener(int index) {
		list.remove(index);
	}

}
