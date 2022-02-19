package org.CommonAlgorithms.CacheElimination.lru;

import org.CommonAlgorithms.CacheElimination.CacheElimination;
import org.CommonAlgorithms.DataStructure.DoubleLinkedListNode;

import java.util.LinkedList;
import java.util.List;

/**
 * @author cartoon
 * @date 2022/2/17 00:07
 */
public class SlruCacheElimination<T> extends AbstractLruCacheElimination<T> implements CacheElimination<T> {

    private DoubleLinkedListNode<T> tempSegment;

    private DoubleLinkedListNode<T> tempSegmentTail;

    private DoubleLinkedListNode<T> protectSegment;

    private DoubleLinkedListNode<T> protectSegmentTail;

    private Integer tempSegmentSize;

    private Integer protectSegmentSize;

    private Integer limitTempSegmentSize;

    private Integer limitProtectSegmentSize;

    @Override
    public boolean put(T element) {
        //element exist
        if(judgeAndMoveElementToTail(tempSegment, tempSegmentTail, element) || judgeAndMoveElementToTail(protectSegment, protectSegmentTail, element)){
            return true;
        }
        if(tempSegmentSize.equals(limitTempSegmentSize) && protectSegmentSize.equals(limitProtectSegmentSize)){
            //移除 protect 区元素
            removeLeastUnusedElement(protectSegment);
            protectSegmentSize--;
            //temp 区入 protect 区
            DoubleLinkedListNode<T> removeFromTempNode = removeAndReturnLeastUnusedElement(tempSegment);
            putElementToTail(protectSegmentTail, removeFromTempNode.getData());
            protectSegmentSize++;
        } else if(tempSegmentSize.equals(limitTempSegmentSize)){
            //temp 区入 protect 区
            DoubleLinkedListNode<T> removeFromTempNode = removeAndReturnLeastUnusedElement(tempSegment);
            putElementToTail(protectSegmentTail, removeFromTempNode.getData());
            protectSegmentSize++;
        }
        // 元素入 temp 区
        putElementToTail(tempSegmentTail, element);
        return true;
    }

    @Override
    public List<T> getAllElement() {
        List<T> res = new LinkedList<>();
        DoubleLinkedListNode<T> traverse = tempSegment.getNextPointer();
        while (traverse != null){
            res.add(traverse.getData());
            traverse = traverse.getNextPointer();
        }
        traverse = protectSegment.getNextPointer();
        while (traverse != null){
            res.add(traverse.getData());
            traverse = traverse.getNextPointer();
        }
        return res;
    }

    @Override
    public int getFreeSpace() {
        return size - tempSegmentSize - protectSegmentSize;
    }

    @Override
    public boolean isFull() {
        return size == (tempSegmentSize + protectSegmentSize);
    }

    public SlruCacheElimination() {
        tempSegment = new DoubleLinkedListNode<>(null, null, null);
        protectSegment = new DoubleLinkedListNode<>(null, null, null);
        tempSegmentTail = tempSegment;
        protectSegmentTail = protectSegment;
        tempSegmentSize = 0;
        protectSegmentSize = 0;
        limitProtectSegmentSize = size / 2;
        limitTempSegmentSize = size - limitProtectSegmentSize;
    }

}
