package ddc.util;

public class Statistics {
	public long itemsProcessed=0;
	public long itemsFailed=0;
	public long bytesProcessed=0;
	public long elapsed=0;
	
	@Override
	public String toString() {
		String info = "";
		info += itemsProcessed != 0 ? " itemsProcessed:[" + itemsProcessed + "]": "";
		info += itemsFailed != 0 ? " itemsFailed:[" + itemsFailed + "]": "";
		info += bytesProcessed != 0 ? " bytesProcessed:[" + bytesProcessed + "]": "";
		info += elapsed != 0 ? " elapsed:[" + elapsed + "(" + Timespan.getHumanReadable(elapsed) + ")]": "";
		return info;
	}
}
