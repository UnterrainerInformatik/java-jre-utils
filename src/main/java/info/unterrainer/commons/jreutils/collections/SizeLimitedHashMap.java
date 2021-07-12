package info.unterrainer.commons.jreutils.collections;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A size-limited hash-map.
 */
public class SizeLimitedHashMap<K, V> extends LinkedHashMap<K, V> {
	private static final long serialVersionUID = 6994714918898609875L;

	private final int maxSize;

	public SizeLimitedHashMap(final int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
		return size() > maxSize;
	}
}
