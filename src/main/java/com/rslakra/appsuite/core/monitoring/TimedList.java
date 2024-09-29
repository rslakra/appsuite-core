package com.rslakra.appsuite.core.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

/**
 * A list implementation where items are only in the list for a certain amount of time.
 *
 * @param <T>
 * @author Rohtash Lakra
 */
public class TimedList<T extends Delayed> extends SingleThreadExecutor implements List<T> {

    private static Logger LOGGER = LoggerFactory.getLogger(TimedList.class);
    private final List<T> items;
    private final DelayQueue<T> delayQueue;

    public TimedList() {
        super("TimedList");
        items = new CopyOnWriteArrayList<T>();
        delayQueue = new DelayQueue<T>();
        startRunnable();
    }

    /**
     * @return
     */
    @Override
    protected Runnable getRunnable() {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        T item = delayQueue.take();
                        items.remove(item);
                    } catch (InterruptedException ex) {
                        LOGGER.warn("InterruptedException:{}", ex.getMessage(), ex);
                    }
                }
            }
        };

        return runnable;
    }

    /**
     * @see List#size()
     */
    @Override
    public int size() {
        return items.size();
    }

    /**
     * @see List#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * @see List#contains(Object)
     */
    @Override
    public boolean contains(Object o) {
        return items.contains(o);
    }

    /**
     * @see List#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }

    /**
     * @return
     */
    @Override
    public Object[] toArray() {
        return items.toArray();
    }

    /**
     * @param elements
     * @param <E>
     * @return
     */
    @Override
    public <E> E[] toArray(final E[] elements) {
        return items.toArray(elements);
    }

    /**
     * @param element
     * @return
     */
    @Override
    public boolean add(final T element) {
        delayQueue.put(element);
        return items.add(element);
    }

    /**
     * @param element
     * @return
     */
    @Override
    public boolean remove(final Object element) {
        delayQueue.remove(element);
        return items.remove(element);
    }

    /**
     * @param elements
     * @return
     */
    @Override
    public boolean containsAll(final Collection<?> elements) {
        return items.containsAll(elements);
    }

    /**
     * @param elements
     * @return
     */
    @Override
    public boolean addAll(final Collection<? extends T> elements) {
        delayQueue.addAll(elements);
        return items.addAll(elements);
    }

    /**
     * @param index
     * @param elements
     * @return
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends T> elements) {
        delayQueue.addAll(elements);
        return items.addAll(index, elements);
    }

    /**
     * @param elements
     * @return
     */
    @Override
    public boolean removeAll(final Collection<?> elements) {
        delayQueue.removeAll(elements);
        return items.removeAll(elements);
    }

    /**
     * @param elements
     * @return
     */
    @Override
    public boolean retainAll(final Collection<?> elements) {
        delayQueue.retainAll(elements);
        return items.retainAll(elements);
    }

    /**
     *
     */
    @Override
    public void clear() {
        delayQueue.clear();
        items.clear();
    }

    /**
     * @param index
     * @return
     */
    @Override
    public T get(final int index) {
        return items.get(index);
    }

    /**
     * @param index
     * @param element
     * @return
     */
    @Override
    public T set(final int index, final T element) {
        delayQueue.put(element);
        return items.set(index, element);
    }

    /**
     * @param index
     * @param element
     */
    @Override
    public void add(final int index, final T element) {
        delayQueue.put(element);
        items.add(index, element);
    }

    /**
     * @param index
     * @return
     */
    @Override
    public T remove(final int index) {
        T element = items.get(index);
        delayQueue.remove(element);
        return items.remove(index);
    }

    /**
     * @param element
     * @return
     */
    @Override
    public int indexOf(final Object element) {
        return items.indexOf(element);
    }

    /**
     * @param element
     * @return
     */
    @Override
    public int lastIndexOf(Object element) {
        return items.lastIndexOf(element);
    }

    /**
     * @return
     */
    @Override
    public ListIterator<T> listIterator() {
        return items.listIterator();
    }

    /**
     * @param index
     * @return
     */
    @Override
    public ListIterator<T> listIterator(final int index) {
        return items.listIterator(index);
    }

    /**
     * @param fromIndex
     * @param toIndex
     * @return
     */
    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return items.subList(fromIndex, toIndex);
    }

}
