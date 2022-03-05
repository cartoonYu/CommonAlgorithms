package org.CommonAlgorithms.CacheElimination.lfu;

import org.CommonAlgorithms.CacheElimination.CacheElimination;
import org.CommonAlgorithms.CacheElimination.lru.AbstractLruCacheElimination;
import org.CommonAlgorithms.DataStructure.DoubleLinkedListNode;

import java.util.*;

/**
 * @author cartoon
 * @version 1.0
 * @since 2022/03/03 00:25
 */
public class LfruCacheElimination<T> extends AbstractLruCacheElimination<T> implements CacheElimination<T> {

    private DoubleLinkedListNode<T> protectSegmentHead;

    private DoubleLinkedListNode<T> protectSegmentTail;

    private int protectSegmentCurrentSize;

    private int protectSegmentMaxSize;

    private Map<T, Integer> unprotectSegmentElementToCount;

    private TreeMap<Integer, List<T>> unprotectSegmentCountToElement;

    private int unprotectSegmentCurrentSize;

    private int unprotectSegmentMaxSize;

    @Override
    public boolean put(T element) {
        //if element in protect segment, return true
        if(judgeAndMoveElementToTail(protectSegmentHead, protectSegmentTail, element)){
            return true;
        }
        //if element in unprotect segment, remove element
        if(unprotectSegmentElementToCount.containsKey(element)){
            Integer originCount = unprotectSegmentElementToCount.get(element);
            List<T> elementList = unprotectSegmentCountToElement.get(originCount);
            unprotectSegmentElementToCount.remove(element);
            elementList.remove(element);
        }
        //if both segment full, remove least frequently used unprotect segment
        if(protectSegmentCurrentSize == protectSegmentMaxSize && unprotectSegmentCurrentSize == unprotectSegmentMaxSize){
            Map.Entry<Integer, List<T>> leastFrequentlyUsedList = unprotectSegmentCountToElement.firstEntry();
            if(leastFrequentlyUsedList.getValue().size() == 1){
                T leastFrequentlyUsedElement = leastFrequentlyUsedList.getValue().get(0);
                unprotectSegmentElementToCount.remove(leastFrequentlyUsedElement);
                unprotectSegmentCountToElement.remove(leastFrequentlyUsedList.getKey());
            } else {
                leastFrequentlyUsedList.getValue().remove(0);
            }
        }
        //if protect segment is full, remove least recently used element and put it to unprotect segment
        if(protectSegmentCurrentSize == protectSegmentMaxSize){
            DoubleLinkedListNode<T> unusedElement = removeAndReturnLeastUnusedElement(protectSegmentHead);
            protectSegmentCurrentSize--;
            unprotectSegmentElementToCount.put(unusedElement.getData(), 1);
            List<T> elementList = unprotectSegmentCountToElement.getOrDefault(1, new LinkedList<>());
            elementList.add(unusedElement.getData());
            unprotectSegmentCountToElement.put(1, elementList);
        }
        putElementToTail(protectSegmentTail, element);
        protectSegmentCurrentSize++;
        return true;
    }

    @Override
    public List<T> getAllElement() {
        List<T> res = new ArrayList<>();
        DoubleLinkedListNode<T> temp = protectSegmentHead.getNextPointer();
        while (temp != null){
            res.add(temp.getData());
            temp = temp.getNextPointer();
        }
        res.addAll(unprotectSegmentElementToCount.keySet());
        return res;
    }

    @Override
    public int getFreeSpace() {
        return size - protectSegmentCurrentSize - unprotectSegmentCurrentSize;
    }

    @Override
    public boolean isFull() {
        return size == protectSegmentCurrentSize + unprotectSegmentCurrentSize;
    }

    public LfruCacheElimination() {
        protectSegmentHead = new DoubleLinkedListNode<>(null, null, null);
        protectSegmentTail = protectSegmentHead;
        protectSegmentCurrentSize = 0;
        protectSegmentMaxSize = size / 2;
        unprotectSegmentElementToCount = new HashMap<>();
        unprotectSegmentCountToElement = new TreeMap<>();
        unprotectSegmentCurrentSize = 0;
        unprotectSegmentMaxSize = size - protectSegmentMaxSize;
    }
}
