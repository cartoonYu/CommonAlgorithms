package org.CommonAlgorithms.DataStructure;

/**
 * @author cartoon
 * @date 2022/2/11 00:44
 */
public class DoubleLinkedListNode<T> {

    private T data;

    private DoubleLinkedListNode<T> lastPointer;

    private DoubleLinkedListNode<T> nextPointer;

    public T getData() {
        return data;
    }

    public DoubleLinkedListNode<T> setData(T data) {
        this.data = data;
        return this;
    }

    public DoubleLinkedListNode<T> getLastPointer() {
        return lastPointer;
    }

    public DoubleLinkedListNode<T> setLastPointer(DoubleLinkedListNode<T> lastPointer) {
        this.lastPointer = lastPointer;
        return this;
    }

    public DoubleLinkedListNode<T> getNextPointer() {
        return nextPointer;
    }

    public DoubleLinkedListNode<T> setNextPointer(DoubleLinkedListNode<T> nextPointer) {
        this.nextPointer = nextPointer;
        return this;
    }

    public DoubleLinkedListNode(T data, DoubleLinkedListNode<T> lastPointer, DoubleLinkedListNode<T> nextPointer) {
        this.data = data;
        this.lastPointer = lastPointer;
        this.nextPointer = nextPointer;
    }
}
