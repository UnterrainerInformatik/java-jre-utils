package info.unterrainer.commons.jreutils.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 * A synchronized, size-limited FIFO-queue.
 */
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

	/**
	 * Inserts the specified element into this queue if it is possible to do so
	 * immediately without violating capacity restrictions. When using a
	 * capacity-restricted queue, this method is generally preferable to
	 * {@link #add}, which can fail to insert an element only by throwing an
	 * exception.
	 *
	 * @param e the element to add
	 * @return this instance to provide a fluent interface
	 * @throws ClassCastException       if the class of the specified element
	 *                                  prevents it from being added to this queue
	 * @throws NullPointerException     if the specified element is null and this
	 *                                  queue does not permit null elements
	 * @throws IllegalArgumentException if some property of this element prevents it
	 *                                  from being added to this queue
	 */
	public synchronized DataQueue<T> offer(final T element) {
		queue.offer(element);
		return this;
	}

	/**
	 * Retrieves and removes the head of this queue, or returns {@code null} if this
	 * queue is empty.
	 *
	 * @return the head of this queue, or {@code null} if this queue is empty
	 */
	public synchronized T poll() {
		return queue.poll();
	}

	/**
	 * Retrieves, but does not remove, the head of this queue, or returns
	 * {@code null} if this queue is empty.
	 *
	 * @return the head of this queue, or {@code null} if this queue is empty
	 */
	public synchronized T peek() {
		return queue.peek();
	}

	/**
	 * Clears this queues' contents.
	 */
	public synchronized void clear() {
		queue.clear();
	}

	/**
	 * Gets a copy of this queue as an {@link ArrayList}.
	 *
	 * @return the list
	 */
	public synchronized List<T> getListClone() {
		List<T> list = new ArrayList<>();
		list.addAll(queue);
		return list;
	}

	/**
	 * Gets a copy of this queue as an {@link ArrayList} and then clears this queue
	 * by calling the method {@link #clear()}.
	 *
	 * @return the list
	 */
	public synchronized List<T> getListCloneAndClear() {
		List<T> list = new ArrayList<>();
		list.addAll(queue);
		queue.clear();
		return list;
	}
}
