package info.unterrainer.commons.jreutils.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lombok.NoArgsConstructor;

/**
 * A synchronized, size-limited hash-map.
 */
@NoArgsConstructor
public class DataMap<K, V> {

	private SizeLimitedHashMap<K, V> map;

	public DataMap(final int maxSize) {
		map = new SizeLimitedHashMap<>(maxSize);
	}

	public synchronized DataMap<K, V> put(final K key, final V value) {
		map.put(key, value);
		return this;
	}

	public synchronized Collection<V> values() {
		return map.values();
	}

	public synchronized Set<K> keySet() {
		return map.keySet();
	}

	public synchronized Set<Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	public synchronized boolean containsKey(final Object key) {
		return map.containsKey(key);
	}

	public synchronized boolean containsValue(final Object value) {
		return map.containsValue(value);
	}

	public synchronized boolean isEmpty() {
		return map.isEmpty();
	}

	public synchronized void clear() {
		map.clear();
	}

	public synchronized V get(final K key) {
		return map.get(key);
	}

	public synchronized V remove(final K key) {
		return map.remove(key);
	}

	public synchronized int size() {
		return map.size();
	}

	public synchronized Map<K, V> getMapClone() {
		return new HashMap<>(map);
	}

	public synchronized Map<K, V> getMapCloneAndClear() {
		Map<K, V> clone;
		clone = new HashMap<>(map);
		map.clear();
		return clone;
	}
}
