package org.CommonAlgorithms.CacheElimination.lru;

import org.CommonAlgorithms.DataStructure.DoubleLinkedListNode;

/**
 * @author cartoon
 * @date 2022/2/19 11:19
 */
public class AbstractLruCacheElimination<T> {

    protected boolean putElementToTail(DoubleLinkedListNode<T> tail, T element){
        DoubleLinkedListNode<T> newNode = new DoubleLinkedListNode<>(element, tail, null);
        tail.setNextPointer(newNode);
        tail = tail.getNextPointer();
        return true;
    }

    protected DoubleLinkedListNode<T> removeAndReturnLeastUnusedElement(DoubleLinkedListNode<T> head){
        DoubleLinkedListNode<T> removeNode = head.getNextPointer();
        if(removeNode != null){
            if(removeNode.getNextPointer() == null){
                head.setNextPointer(null);
            } else {
                head.setNextPointer(removeNode.getNextPointer());
                removeNode.getNextPointer().setLastPointer(head);
            }
        }
        return removeNode;
    }

    protected boolean removeLeastUnusedElement(DoubleLinkedListNode<T> head){
        return removeAndReturnLeastUnusedElement(head) != null;
    }

    protected boolean judgeAndMoveElementToTail(DoubleLinkedListNode<T> head, DoubleLinkedListNode<T> tail, T element){
        DoubleLinkedListNode<T> existNode = judgeElementExist(head, element);
        if(existNode != null){
            return moveExistElementToTail(tail, existNode);
        }
        return false;
    }

    private DoubleLinkedListNode<T> judgeElementExist(DoubleLinkedListNode<T> head, T element){
        DoubleLinkedListNode<T> traverse = head.getNextPointer();
        DoubleLinkedListNode<T> existNode = null;
        while (traverse != null){
            if(traverse.getData().equals(element)){
                existNode = traverse;
                break;
            }
            traverse = traverse.getNextPointer();
        }
        return existNode;
    }

    private boolean moveExistElementToTail(DoubleLinkedListNode<T> tail, DoubleLinkedListNode<T> existNode){
        existNode.getLastPointer().setNextPointer(existNode.getNextPointer());
        if(existNode.getNextPointer() != null){
            existNode.getNextPointer().setLastPointer(existNode.getLastPointer());
        }
        existNode.setLastPointer(tail);
        tail.setNextPointer(existNode);
        tail = tail.getNextPointer();
        return true;
    }

}
