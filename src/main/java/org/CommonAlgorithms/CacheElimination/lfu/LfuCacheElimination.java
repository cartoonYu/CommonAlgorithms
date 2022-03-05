package org.CommonAlgorithms.CacheElimination.lfu;

import org.CommonAlgorithms.CacheElimination.CacheElimination;

import java.util.*;

/**
 * @author cartoon
 * @version 1.0
 * @since 2022/03/02 23:32
 */
public class LfuCacheElimination<T> implements CacheElimination<T> {

    private Map<T, Integer> elementToCount;

    private TreeMap<Integer, List<T>> countToElement;

    @Override
    public boolean put(T element) {
        //if cache is full, remove the least used element random
        if(elementToCount.size() == size){
            Map.Entry<Integer, List<T>> toBeRemoveList = countToElement.firstEntry();
            toBeRemoveList.getValue().remove(0);
        }
        //get source data
        Integer originCount = elementToCount.getOrDefault(element, 0);
        Integer currentCount = originCount + 1;
        List<T> originElementList = countToElement.getOrDefault(originCount, new ArrayList<>());
        List<T> currentElementList = countToElement.getOrDefault(originCount + 1, new ArrayList<>());
        //operate
        originElementList.remove(element);
        currentElementList.add(element);
        elementToCount.put(element, currentCount);
        if(originElementList.isEmpty()){
            countToElement.remove(originCount);
        } else {
            countToElement.put(originCount, originElementList);
        }
        countToElement.put(currentCount, currentElementList);
        return false;
    }

    @Override
    public List<T> getAllElement() {
        return new ArrayList<>(elementToCount.keySet());
    }

    @Override
    public int getFreeSpace() {
        return size - elementToCount.size();
    }

    @Override
    public boolean isFull() {
        return size == elementToCount.size();
    }

    public LfuCacheElimination() {
        elementToCount = new HashMap<>(size);
        countToElement = new TreeMap<>();
    }

}
