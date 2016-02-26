package ddc.util;

public class Statistics {
	public String name="";
	public long timestamp=0;
	public long itemsProcessed=0;
	public long itemsFailed=0;
	public long bytesProcessed=0;
	public long elapsed=0;
	public String message="";
	
	@Override
	public String toString() {
		String info = name;
		info += timestamp != 0 ? " timestamp:[" + timestamp + "]": "";
		info += itemsProcessed != 0 ? " itemsProcessed:[" + itemsProcessed + "]": "";
		info += itemsFailed != 0 ? " itemsFailed:[" + itemsFailed + "]": "";
		info += bytesProcessed != 0 ? " bytesProcessed:[" + bytesProcessed + "]": "";
		info += elapsed != 0 ? " elapsed:[" + elapsed + "(" + Timespan.getHumanReadable(elapsed) + ")]": "";
		info += message != "" ? " message:[" + message + "]": "";
		return info;
	}
}
