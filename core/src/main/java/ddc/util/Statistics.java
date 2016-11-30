package ddc.util;

public class Statistics {
	public Chronometer chron = new Chronometer();
	public long itemsProcessed=0;
	public long itemsAffected=0;
	public long itemsFailed=0;
	public long bytesProcessed=0;
	
	public String getElapsed() {
		return chron.toString();
	}
	
	public String getBytesOverSecs() {
		if (chron.getElapsed()==0) return "";
		if (bytesProcessed==0) return "";
		long ratio = (long)bytesProcessed / chron.getElapsed()*1000;
		if (ratio==0) return "";
		return ratio + " bytes/sec (" +  FileUtils.byteCountToDisplaySize(ratio) + "/sec)";
	}

	public String getItemsOverSecs() {
		if (chron.getElapsed()==0) return "";
		if (itemsProcessed==0) return "";
		long ratio = itemsProcessed / chron.getElapsed()*1000;
		if (ratio==0) return "";
		return String.valueOf(ratio)  + " items/sec";
	}
	
	public String getProcessedHumanReadable() {
		return FileUtils.byteCountToDisplaySize(bytesProcessed);
	}
	
	@Override
	public String toString() {
		String info = "";
		long itemsSucceeded = itemsProcessed-itemsFailed;
		info += " itemsProcessed:[" + itemsProcessed + "]";
		info += itemsSucceeded != 0 ? " itemsSucceeded:[" + itemsSucceeded + "]" : "";
		info += itemsAffected != 0 ? " itemsAffected:[" + itemsAffected + "]" : "";
		info += itemsFailed != 0 ? " itemsFailed:[" + itemsFailed + "]" : "";
		info += bytesProcessed != 0 ? " bytesProcessed:[" + bytesProcessed + " (" + getProcessedHumanReadable() + ")]" : "";
		info += chron != null ? " elapsed:[" + chron.toString() + "]": "";
		info += getBytesOverSecs()!="" ? " [" + getBytesOverSecs() + "]": "";
		info += getItemsOverSecs()!="" ? " :[" + getItemsOverSecs() + "]": "";
		return info.trim();
	}
}
