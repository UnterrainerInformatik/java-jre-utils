package info.unterrainer.commons.jreutils.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.queue.CircularFifoQueue;

public class DataQueue<T> {

	private CircularFifoQueue<T> queue;

	public DataQueue(final int maxSize) {
		this(maxSize, (Collection<T>) null);
	}

	public DataQueue(final int maxSize, final Collection<T> collection) {
		queue = new CircularFifoQueue<>(maxSize);
		if (collection != null)
			for (T element : collection)
				queue.offer(element);
	}

	public DataQueue(final int maxSize, final T[] array) {
		queue = new CircularFifoQueue<>(maxSize);
		if (array != null)
			for (T element : array)
				queue.offer(element);
	}

	public synchronized DataQueue<T> offer(final T element) {
		queue.offer(element);
		return this;
	}

	public synchronized T poll() {
		return queue.poll();
	}

	public synchronized T peek() {
		return queue.peek();
	}

	public synchronized void clear() {
		queue.clear();
	}

	public synchronized List<T> getListClone() {
		List<T> list = new ArrayList<>();
		list.addAll(queue);
		return list;
	}

	public synchronized List<T> getListCloneAndClear() {
		List<T> list = new ArrayList<>();
		list.addAll(queue);
		queue.clear();
		return list;
	}
}
