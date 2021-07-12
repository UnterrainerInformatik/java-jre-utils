package info.unterrainer.commons.jreutils.collections;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class DataTable<T> {

	private Class<T> clazz;
	private int maxEntries;
	@Getter
	private DataQueue<T> queue;

	private HashMap<String, Function<T, Object>> keySuppliers = new HashMap<>();
	private HashMap<String, DataMap<Object, T>> maps = new HashMap<>();

	public DataTable(final Class<T> clazz, final int maxEntries) {
		this.clazz = clazz;
		this.maxEntries = maxEntries;
		queue = new DataQueue<>(maxEntries);
	}

	@SuppressWarnings("unchecked")
	public <K> DataTable<T> addIndex(final String name, final Function<T, K> keySupplier) {
		keySuppliers.put(name, (Function<T, Object>) keySupplier);
		maps.put(name, new DataMap<Object, T>(maxEntries));
		return this;
	}

	public synchronized T peek() {
		return queue.peek();
	}

	public synchronized T poll() {
		T e = queue.poll();
		for (String name : keySuppliers.keySet()) {
			Function<T, ?> func = keySuppliers.get(name);
			DataMap<Object, T> map = maps.get(name);
			map.remove(func.apply(e));
		}
		return e;
	}

	public synchronized <K> T get(final String name, final K key) {
		return maps.get(name).get(key);
	}

	public synchronized void add(final T element) {
		queue.offer(element);
		for (String name : keySuppliers.keySet()) {
			Function<T, ?> func = keySuppliers.get(name);
			DataMap<Object, T> map = maps.get(name);
			Object key = func.apply(element);
			map.put(key, element);
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized <K> Set<K> keySet(final String name) {
		return (Set<K>) maps.get(name).keySet();
	}

	public synchronized Collection<T> values(final String name) {
		return maps.get(name).values();
	}

	public synchronized boolean containsValue(final String name, final T value) {
		return maps.get(name).containsValue(value);
	}

	public synchronized <K> boolean containsKey(final String name, final K value) {
		return maps.get(name).containsKey(value);
	}

	public synchronized void clear() {
		queue.clear();
		for (String name : keySuppliers.keySet())
			maps.get(name).clear();
	}

	@SuppressWarnings("unchecked")
	public synchronized DataTable<T> load(T[] backingArray) {
		if (backingArray == null)
			backingArray = (T[]) Array.newInstance(clazz, 0);

		queue = new DataQueue<>(maxEntries, backingArray);

		for (String name : keySuppliers.keySet()) {
			DataMap<Object, T> map = new DataMap<>(maxEntries);
			maps.put(name, map);
			Function<T, ?> func = keySuppliers.get(name);
			for (T s : backingArray)
				map.put(func.apply(s), s);
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public synchronized T[] toArray() {
		T[] zeroArray = (T[]) Array.newInstance(clazz, 0);
		return queue.getListClone().toArray(zeroArray);
	}
}
