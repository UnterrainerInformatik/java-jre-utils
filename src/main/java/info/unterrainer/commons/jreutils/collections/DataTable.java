package info.unterrainer.commons.jreutils.collections;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * A synchronized data-structure acting as a table. You may create indexes for
 * various columns and later on retrieve sets of index-keys or the indexed
 * values.<br>
 * You also may specify fragmenting indexes, that define an additional filter
 * that then is applied on inserting a value. The value will then only be added
 * to a specific index, if the filter is satisfied. This allows for
 * runtime-efficient table-fragmentation.
 */
@Accessors(fluent = true)
public class DataTable<T> {

	private Class<T> clazz;
	private int maxEntries;
	@Getter
	private DataQueue<T> queue;

	private HashMap<String, Function<T, Object>> keySuppliers = new HashMap<>();
	private HashMap<String, Function<T, Boolean>> filters = new HashMap<>();
	private HashMap<String, DataMap<Object, T>> maps = new HashMap<>();

	private HashMap<String, Function<T, Object>> multiKeySuppliers = new HashMap<>();
	private HashMap<String, Function<T, Boolean>> multiFilters = new HashMap<>();
	private HashMap<String, MultiValuedMap<Object, T>> multiMaps = new HashMap<>();

	public DataTable(final Class<T> clazz, final int maxEntries) {
		this.clazz = clazz;
		this.maxEntries = maxEntries;
		queue = new DataQueue<>(maxEntries);
	}

	public <K> DataTable<T> addIndex(final String name, final Function<T, K> keySupplier) {
		return addIndex(name, keySupplier, null);
	}

	public <K> DataTable<T> addMultiIndex(final String name, final Function<T, K> keySupplier) {
		return addMultiIndex(name, keySupplier, null);
	}

	@SuppressWarnings("unchecked")
	public <K> DataTable<T> addIndex(final String name, final Function<T, K> keySupplier,
			final Function<T, Boolean> filter) {
		keySuppliers.put(name, (Function<T, Object>) keySupplier);
		if (filter != null)
			filters.put(name, filter);
		maps.put(name, new DataMap<>(maxEntries));
		return this;
	}

	@SuppressWarnings("unchecked")
	public <K> DataTable<T> addMultiIndex(final String name, final Function<T, K> keySupplier,
			final Function<T, Boolean> filter) {
		multiKeySuppliers.put(name, (Function<T, Object>) keySupplier);
		if (filter != null)
			multiFilters.put(name, filter);
		multiMaps.put(name, new ArrayListValuedHashMap<>(maxEntries));
		return this;
	}

	/**
	 * Retrieves, but does not remove, the head (first inserted element) of this
	 * DataTable, or returns {@code null} if this DataTable is empty.
	 *
	 * @return the head of this DataTable, or {@code null} if this DataTable is
	 *         empty
	 */
	public synchronized T peek() {
		return queue.peek();
	}

	/**
	 * Retrieves and removes the head (first inserted element) of this DataTable, or
	 * returns {@code null} if this DataTable is empty.
	 *
	 * @return the head of this queue, or {@code null} if this DataTable is empty
	 */
	public synchronized T poll() {
		T e = queue.poll();
		for (String name : keySuppliers.keySet()) {
			Function<T, ?> func = keySuppliers.get(name);
			DataMap<Object, T> map = maps.get(name);
			map.remove(func.apply(e));
		}
		for (String name : multiKeySuppliers.keySet()) {
			Function<T, ?> func = multiKeySuppliers.get(name);
			MultiValuedMap<Object, T> map = multiMaps.get(name);
			map.removeMapping(func.apply(e), e);
		}
		return e;
	}

	/**
	 * Gets an element by a specified index.
	 *
	 * @param <K>  the type of the key used by the given index
	 * @param name the name of the index
	 * @param key  the key of the element to retrieve using the given index
	 * @return the element to retrieve
	 */
	public synchronized <K> T get(final String name, final K key) {
		return maps.get(name).get(key);
	}

	/**
	 * Gets elements by a specified multi-index.
	 *
	 * @param <K>  the type of the key used by the given multi-index
	 * @param name the name of the multi-index
	 * @param key  the key of the element to retrieve using the given multi-index
	 * @return the collection of elements to retrieve, that may be empty
	 */
	public synchronized <K> Collection<T> multiGet(final String name, final K key) {
		return multiMaps.get(name).get(key);
	}

	/**
	 * Adds one or more elements to the DataTable.
	 * <p>
	 * The element then gets properly indexed and added to the given indexes.<br>
	 * If there is a filter for an index, the element is only added to that index,
	 * if the filter is satisfied.
	 *
	 * @param elements the elements to insert
	 */
	@SuppressWarnings("unchecked")
	public synchronized void add(final T... elements) {
		for (T element : elements) {
			queue.offer(element);
			for (Entry<String, Function<T, Object>> entry : keySuppliers.entrySet()) {
				Function<T, Boolean> filter = filters.get(entry.getKey());
				DataMap<Object, T> map = maps.get(entry.getKey());
				if (filter == null || filter.apply(element)) {
					Object key = entry.getValue().apply(element);
					map.put(key, element);
				}
			}
			for (Entry<String, Function<T, Object>> entry : multiKeySuppliers.entrySet()) {
				Function<T, Boolean> filter = multiFilters.get(entry.getKey());
				MultiValuedMap<Object, T> map = multiMaps.get(entry.getKey());
				if (filter == null || filter.apply(element)) {
					Object key = entry.getValue().apply(element);
					map.put(key, element);
				}
			}
		}
	}

	/**
	 * Get a set of keys for a given index.
	 *
	 * @param <K>  the type of the key for the given index
	 * @param name the name of the index to get the keys from
	 * @return a set of keys
	 */
	@SuppressWarnings("unchecked")
	public synchronized <K> Set<K> keySet(final String name) {
		return (Set<K>) maps.get(name).keySet();
	}

	/**
	 * Get a set of keys for a given multi-index.
	 *
	 * @param <K>  the type of the key for the given multi-index
	 * @param name the name of the multi-index to get the keys from
	 * @return a set of keys
	 */
	@SuppressWarnings("unchecked")
	public synchronized <K> Set<K> multiKeySet(final String name) {
		return (Set<K>) multiMaps.get(name).keySet();
	}

	/**
	 * Get a list of keys for a given index.
	 *
	 * @param <K>  the type of the key for the given index
	 * @param name the name of the index to get the keys from
	 * @return a list of keys
	 */
	@SuppressWarnings("unchecked")
	public synchronized <K> List<K> keyList(final String name) {
		List<K> list = new ArrayList<>();
		list.addAll((Set<K>) maps.get(name).keySet());
		return list;
	}

	/**
	 * Get a list of keys for a given multi-index.
	 *
	 * @param <K>  the type of the key for the given multi-index
	 * @param name the name of the multi-index to get the keys from
	 * @return a list of keys
	 */
	@SuppressWarnings("unchecked")
	public synchronized <K> List<K> multiKeyList(final String name) {
		List<K> list = new ArrayList<>();
		list.addAll((Set<K>) multiMaps.get(name).keySet());
		return list;
	}

	public synchronized Collection<T> values(final String name) {
		return maps.get(name).values();
	}

	public synchronized Collection<T> multiValues(final String name) {
		return multiMaps.get(name).values();
	}

	public synchronized boolean containsValue(final String name, final T value) {
		return maps.get(name).containsValue(value);
	}

	public synchronized boolean multiContainsValue(final String name, final T value) {
		return multiMaps.get(name).containsValue(value);
	}

	public synchronized <K> boolean containsKey(final String name, final K value) {
		return maps.get(name).containsKey(value);
	}

	public synchronized <K> boolean multiContainsKey(final String name, final K value) {
		return multiMaps.get(name).containsKey(value);
	}

	public synchronized void clear() {
		queue.clear();
		for (String name : keySuppliers.keySet())
			maps.get(name).clear();
		for (String name : multiKeySuppliers.keySet())
			multiMaps.get(name).clear();
	}

	@SuppressWarnings("unchecked")
	public synchronized DataTable<T> load(T[] backingArray) {
		if (backingArray == null)
			backingArray = (T[]) Array.newInstance(clazz, 0);

		queue = new DataQueue<>(maxEntries, backingArray);
		for (Entry<String, Function<T, Object>> entry : keySuppliers.entrySet()) {
			DataMap<Object, T> map = new DataMap<>(maxEntries);
			maps.put(entry.getKey(), map);
			Function<T, Boolean> filter = filters.get(entry.getKey());
			for (T s : backingArray)
				if (filter == null || filter.apply(s))
					map.put(entry.getValue().apply(s), s);
		}
		for (Entry<String, Function<T, Object>> entry : multiKeySuppliers.entrySet()) {
			MultiValuedMap<Object, T> map = new ArrayListValuedHashMap<>(maxEntries);
			multiMaps.put(entry.getKey(), map);
			Function<T, Boolean> filter = multiFilters.get(entry.getKey());
			for (T s : backingArray)
				if (filter == null || filter.apply(s))
					map.put(entry.getValue().apply(s), s);
		}
		return this;
	}

	/**
	 * Gets a list of all the elements in this DataTable as an detached
	 * offline-copy.
	 *
	 * @return the list
	 */
	public synchronized List<T> toList() {
		return queue.getListClone();
	}

	/**
	 * Get a list of the elements in this DataTable, that are in the given index, as
	 * an detached offline-copy.
	 *
	 * @param name the name of the index to get the elements from
	 * @return the list
	 */
	public synchronized List<T> toList(final String name) {
		List<T> list = new ArrayList<>();
		list.addAll(maps.get(name).values());
		return list;
	}

	/**
	 * Gets an array of all the elements in this DataTable as an detached
	 * offline-copy.
	 *
	 * @return the array
	 */
	@SuppressWarnings("unchecked")
	public synchronized T[] toArray() {
		T[] zeroArray = (T[]) Array.newInstance(clazz, 0);
		return queue.getListClone().toArray(zeroArray);
	}

	/**
	 * Get an array of the elements in this DataTable, that are in the given index,
	 * as an detached offline-copy.
	 *
	 * @param name the name of the index to get the elements from
	 * @return the array
	 */
	@SuppressWarnings("unchecked")
	public synchronized T[] toArray(final String name) {
		T[] zeroArray = (T[]) Array.newInstance(clazz, 0);
		return toList(name).toArray(zeroArray);
	}
}
