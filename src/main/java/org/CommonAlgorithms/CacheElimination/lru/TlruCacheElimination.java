package org.CommonAlgorithms.CacheElimination.lru;

import org.CommonAlgorithms.CacheElimination.CacheElimination;

import java.util.*;

/**
 * @author cartoon
 * @date 2022/2/13 17:33
 */
public class TlruCacheElimination<T> implements CacheElimination<T> {

    private TreeMap<Integer, List<T>> timeToElementMap;

    private Map<T, Integer> elementToTimeMap;

    private Integer currentSize;

    private Integer expireTime;

    @Override
    public boolean put(T element) {
        if(!elementToTimeMap.containsKey(element)){
            if(currentSize.equals(size)){
                List<T> dataList = timeToElementMap.get(0);
                T removeData = dataList.get(0);
                elementToTimeMap.remove(removeData);
                dataList.remove(0);
                currentSize--;
            }
            currentSize++;
        }
        for(T originElement : elementToTimeMap.keySet()){
            elementToTimeMap.put(originElement, elementToTimeMap.get(originElement) - 1);
        }
        elementToTimeMap.put(element, expireTime);
        initTimeToElementMap();
        elementToTimeMap.forEach((originElement, time) -> {
            List<T> dataList = timeToElementMap.get(time);
            dataList.add(originElement);
        });
        return true;
    }

    @Override
    public List<T> getAllElement() {
        return new ArrayList<>(elementToTimeMap.keySet());
    }

    @Override
    public int getFreeSpace() {
        return size - currentSize;
    }

    @Override
    public boolean isFull() {
        return size.equals(currentSize);
    }

    private void initTimeToElementMap(){
        timeToElementMap = new TreeMap<>();
        for(int index = 0; index <= expireTime; index++){
            timeToElementMap.put(index, new ArrayList<>());
        }
    }

    public TlruCacheElimination(){
        currentSize = 0;
        expireTime = 5;
        elementToTimeMap = new HashMap<>();
        initTimeToElementMap();
    }
}
