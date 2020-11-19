package Map;

public class InputMap {

    /**
     * start int map
     */
    private int start;

    /**
     * end in map
     */
    private int end;

    /**
     * length between start and end
     */
    private int length;

    public int getStart() {
        return start;
    }

    public InputMap setStart(int start) {
        this.start = start;
        return this;
    }

    public int getEnd() {
        return end;
    }

    public InputMap setEnd(int end) {
        this.end = end;
        return this;
    }

    public int getLength() {
        return length;
    }

    public InputMap setLength(int length) {
        this.length = length;
        return this;
    }
}
