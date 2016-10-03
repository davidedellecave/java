package ddc.util;

public class Statistics {
	public Chronometer chron = new Chronometer();
	public long itemsProcessed=0;
	public long itemsFailed=0;
	public long bytesProcessed=0;
	
	public String getElapsed() {
		return chron.toString();
	}
	
	@Override
	public String toString() {
		String info = "";
		info += itemsProcessed != 0 ? " itemsProcessed:[" + itemsProcessed + "]": "";
		info += itemsFailed != 0 ? " itemsFailed:[" + itemsFailed + "]": "";
		info += bytesProcessed != 0 ? " bytesProcessed:[" + bytesProcessed + "]": "";
		info += chron != null ? " elapsed:[" + chron.getElapsed() + "(" + chron.toString() + ")]": "";
		return info;
	}
}
