package ddc.util;

import java.util.HashMap;
import java.util.Map;

public class LiteCache {
	private static Map<String, CacheItem> cache = new HashMap<>();

	public synchronized static Object get(String name) {
		if (cache.containsKey(name)) {
			CacheItem item = cache.get(name);
			if (item.chron.isCountdownOver()) {
				cache.remove(name);
				return null;
			} else {
				return item.obj;
			}
		}
		return null;
	}

	public synchronized static void put(String name, Object obj, long evictionMillis) {
		CacheItem item = (new LiteCache()).new CacheItem();
		item.chron = new Chronometer(evictionMillis);
		item.obj = obj;
		cache.put(name, item);
	}

	public synchronized static boolean isEvicted(String name) {
		if (cache.containsKey(name)) {
			CacheItem item = cache.get(name);
			return item.chron.isCountdownOver();
		}
		return true;
	}
	
	public synchronized static void clear() {
		cache.clear();
	}

	class CacheItem {
		public Chronometer chron;
		public Object obj;
	}
}
