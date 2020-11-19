package Map.OutputStructure;

public class AdjacencyListNode {

    private int point;

    private int data;

    private AdjacencyListNode next;

    public int getPoint() {
        return point;
    }

    public AdjacencyListNode setPoint(int point) {
        this.point = point;
        return this;
    }

    public int getData() {
        return data;
    }

    public AdjacencyListNode setData(int data) {
        this.data = data;
        return this;
    }

    public AdjacencyListNode getNext() {
        return next;
    }

    public AdjacencyListNode setNext(AdjacencyListNode next) {
        this.next = next;
        return this;
    }
}
