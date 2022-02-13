package org.CommonAlgorithms.CacheElimination;

import java.util.*;

/**
 * @author cartoon
 * @date 2022/2/9 22:30
 */
public class FiloCacheElimination<T> implements CacheElimination<T>{

    private Deque<T> deque;

    @Override
    public boolean put(T element) {
        if(isFull()){
            deque.removeLast();
        }
        deque.offer(element);
        return true;
    }

    @Override
    public List<T> getAllElement() {
        return new ArrayList<>(deque);
    }

    @Override
    public int getFreeSpace() {
        return size - deque.size();
    }

    @Override
    public boolean isFull() {
        return size == deque.size();
    }

    public FiloCacheElimination() {
        deque = new ArrayDeque<>(size);
    }
}
