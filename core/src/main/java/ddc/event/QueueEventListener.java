package ddc.event;

public interface QueueEventListener {
	public void onStart(int size);
	public void onStopInput(int size);
	public void onClose(int size);
	public void onPut(EventData event, int size);
	public void onPool(EventData event, int size);
}
