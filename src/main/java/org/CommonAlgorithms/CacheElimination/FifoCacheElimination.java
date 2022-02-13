package org.CommonAlgorithms.CacheElimination;

import java.util.*;

/**
 * @author cartoon
 * @date 2022/2/9 20:38
 */
public class FifoCacheElimination<T> implements CacheElimination<T>{

    private Queue<T> queue;

    @Override
    public boolean put(T element) {
        if(isFull()){
            queue.poll();
        }
        queue.offer(element);
        return true;
    }

    @Override
    public List<T> getAllElement() {
        return new ArrayList<>(queue);
    }

    @Override
    public int getFreeSpace() {
        return size - queue.size();
    }

    @Override
    public boolean isFull() {
        return queue.size() == size;
    }

    public FifoCacheElimination() {
        queue = new ArrayDeque<>(size);
    }
}
