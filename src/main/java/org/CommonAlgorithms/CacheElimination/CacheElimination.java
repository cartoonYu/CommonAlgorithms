package org.CommonAlgorithms.CacheElimination;

import java.util.List;

/**
 * @author cartoon
 * @date 2022/2/9 11:43
 */
public interface CacheElimination<T> {

    Integer size = 10;

    boolean put(T element);

    List<T> getAllElement();

    int getFreeSpace();

    boolean isFull();
}
