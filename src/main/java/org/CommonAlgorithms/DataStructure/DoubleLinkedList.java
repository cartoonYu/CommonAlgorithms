package org.CommonAlgorithms.DataStructure;

/**
 * @author cartoon
 * @date 2022/2/11 00:44
 */
public class DoubleLinkedList<T> {

    private T data;

    private DoubleLinkedList<T> lastPointer;

    private DoubleLinkedList<T> nextPointer;

    public T getData() {
        return data;
    }

    public DoubleLinkedList<T> setData(T data) {
        this.data = data;
        return this;
    }

    public DoubleLinkedList<T> getLastPointer() {
        return lastPointer;
    }

    public DoubleLinkedList<T> setLastPointer(DoubleLinkedList<T> lastPointer) {
        this.lastPointer = lastPointer;
        return this;
    }

    public DoubleLinkedList<T> getNextPointer() {
        return nextPointer;
    }

    public DoubleLinkedList<T> setNextPointer(DoubleLinkedList<T> nextPointer) {
        this.nextPointer = nextPointer;
        return this;
    }

    public DoubleLinkedList(T data, DoubleLinkedList<T> lastPointer, DoubleLinkedList<T> nextPointer) {
        this.data = data;
        this.lastPointer = lastPointer;
        this.nextPointer = nextPointer;
    }
}
