package org.CommonAlgorithms.CacheElimination.lru;

import org.CommonAlgorithms.CacheElimination.CacheElimination;
import org.CommonAlgorithms.DataStructure.DoubleLinkedListNode;

import java.util.List;

/**
 * @author cartoon
 * @date 2022/2/11 00:39
 */
public class LruCacheElimination<T> extends AbstractLruCacheElimination<T> implements CacheElimination<T> {

    private DoubleLinkedListNode<T> head;

    private DoubleLinkedListNode<T> tail;

    private int currentSize;

    @Override
    public boolean put(T element) {
        if(judgeAndMoveElementToTail(head, tail, element)){
            return true;
        }
        if(isFull()){
            //踢出元素
            boolean removeLeastUnusedElementRes = removeLeastUnusedElement(head);
            currentSize = removeLeastUnusedElementRes ? currentSize - 1 : currentSize;
        }
        boolean putElementRes = putElementToTail(tail, element);
        currentSize = putElementRes ? currentSize + 1 : currentSize;
        return true;
    }

    @Override
    public List<T> getAllElement() {
        return null;
    }

    @Override
    public int getFreeSpace() {
        return size - currentSize;
    }

    @Override
    public boolean isFull() {
        return size == currentSize;
    }

    public LruCacheElimination() {
        head = new DoubleLinkedListNode<>(null, null, null);
        tail = head;
        currentSize = 0;
    }
}
