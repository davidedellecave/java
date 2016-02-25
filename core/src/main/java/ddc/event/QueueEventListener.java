package ddc.event;

public interface QueueEventListener {
	public void onStart(int size);
	public void onStopInput(int size);
	public void onClose(int size);
	public void onPut(DataEvent event, int size);
	public void onPool(DataEvent event, int size);
}
