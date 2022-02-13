package org.CommonAlgorithms.CacheElimination;

import org.CommonAlgorithms.DataStructure.DoubleLinkedList;

import java.util.List;

/**
 * @author cartoon
 * @date 2022/2/11 00:39
 */
public class LruCacheElimination<T> implements CacheElimination<T>{

    private DoubleLinkedList<T> head;

    private int currentSize;

    @Override
    public boolean put(T element) {
        DoubleLinkedList<T> traverse = head.getNextPointer();
        DoubleLinkedList<T> existNode = null;
        while (traverse != null){
            if(traverse.getData().equals(element)){
                existNode = traverse;
                break;
            }
            traverse = traverse.getNextPointer();
        }
        if(existNode != null){
            existNode.getLastPointer().setNextPointer(existNode.getNextPointer());
            if(existNode.getNextPointer() != null){
                existNode.getNextPointer().setLastPointer(existNode.getLastPointer());
            }
            if(head.getNextPointer() != null){
                head.getNextPointer().setLastPointer(existNode);
            }
            existNode.setNextPointer(head.getNextPointer());
            existNode.setLastPointer(head);
            head.setNextPointer(existNode);
            return true;
        }
        if(isFull()){
            //踢出元素
            DoubleLinkedList<T> removeNode = head.getNextPointer();
            if(removeNode != null){
                if(removeNode.getNextPointer() == null){
                    head.setNextPointer(null);
                } else {
                    head.setNextPointer(removeNode.getNextPointer());
                    removeNode.getNextPointer().setLastPointer(head);
                }
                removeNode = null;
                currentSize--;
            }
        }
        traverse = head;
        while (traverse.getNextPointer() != null){
            traverse = traverse.getNextPointer();
        }
        DoubleLinkedList<T> newNode = new DoubleLinkedList<>(element, traverse, null);
        traverse.setNextPointer(newNode);
        currentSize++;
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
        head = new DoubleLinkedList<>(null, null, null);
        currentSize = 0;
    }
}
