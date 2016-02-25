package ddc.event;

import org.apache.commons.io.FileUtils;

import ddc.util.Chronometer;
import ddc.util.Statistics;

public class StatsQueueEventListener implements QueueEventListener {
	private Statistics stats = new Statistics();
	private Chronometer chron = new Chronometer();
	private DataEvent lastPut = null;
	private DataEvent lastPool = null;
	private long queueSize = 0;
	@Override
	public void onStart(int items) {
		queueSize = items;
		chron.start();
	}

	@Override
	public void onStopInput(int items) {
		queueSize = items;
	}

	@Override
	public void onClose(int items) {
		queueSize = items;
		chron.stop();
		stats.elapsed = chron.getElapsed();
	}

	@Override
	public void onPut(DataEvent event, int items) {
		queueSize = items;
		lastPut = event;
	}

	@Override
	public void onPool(DataEvent event, int items) {
		queueSize = items;
		stats.itemsProcessed++;
		stats.bytesProcessed += event.getData().length;
		lastPool = event;
	}

	public long getElapsed() {
		return chron.getElapsed();
	}

	public long getQueueSize() {
		return queueSize;
	}

	public long getProcessedItem() {
		return stats.itemsProcessed;
	}

	public long getProcessedBytes() {
		return stats.bytesProcessed;
	}

	public DataEvent getLastPut() {
		return lastPut;
	}

	public DataEvent getLastPool() {
		return lastPool;
	}

	/**
	 * @return items/secs
	 */
	public double getItemsThroughput() {
		long secs = getElapsed() * 1000;
		if (secs > 0) {
			return getProcessedItem() / secs;
		} else
			return 0;
	}

	/**
	 * @return bytes/secs
	 */
	public double getBytesThroughput() {
		long secs = getElapsed() * 1000;
		if (secs > 0) {
			return getProcessedBytes() / secs;
		} else
			return 0;
	}

	@Override
	public String toString() {
		String info = "processed#:[" + getProcessedItem() + "]";
		info += " processed bytes:[" + getProcessedBytes() + " (" + FileUtils.byteCountToDisplaySize(getProcessedBytes()) + ")]";
		info += " elapsed:[" + chron.toString() + "]";
//		info += " throughput #:[" + getItemsThroughput() + "]";
//		info += " throughput bytes:[" + getBytesThroughput() + "]";
		info += " current items:[" + getQueueSize() + "]";
		return info;
	}

}
