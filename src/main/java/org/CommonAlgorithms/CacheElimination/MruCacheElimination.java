package org.CommonAlgorithms.CacheElimination;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @author cartoon
 * @version 1.0
 * @since 2022/03/07 22:46
 */
public class MruCacheElimination<T> implements CacheElimination<T>{

    private Deque<T> deque;

    @Override
    public boolean put(T element) {
        if(deque.size() == size){
            deque.removeLast();
        }
        deque.addLast(element);
        return true;
    }

    @Override
    public List<T> getAllElement() {
        return new LinkedList<>(deque);
    }

    @Override
    public int getFreeSpace() {
        return size - deque.size();
    }

    @Override
    public boolean isFull() {
        return size == deque.size();
    }

    public MruCacheElimination() {
        deque = new ArrayDeque<>(size);
    }
}
