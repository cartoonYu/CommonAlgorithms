package Sort;

import Sort.util.SwapElement;

public class InsertSort {

    /**
     * 1. About Complexity
     *     1.1 Best time Complexity is O(n)
     *     1.2 Worst time Complexity is O(n^2)
     *     1.3 Average time Complexity is O(n^2)
     *     1.2 Space Complexity is O(1)
     * 2. Stability
     *     2.1 stable
     * 3. how it run
     *     3.1 use a integer to cache current element in surrounding loop
     *     3.2 use double circulation to move element
     *            3.2.1 surrounding loop is to get element to be sorted
     *            3.2.2 inner loop is in sorting area,and insert position is obtained by comparing cache integer
     *            3.2.3 insert cache integer to 3.2.2 calculation position
     * 4. Q&A
     * @param data
     */
    public static void sort(int[] data) {
        if (data.length < 2) {
            return;
        }
        int value, j;
        for (int i = 1, length = data.length; i < length; i++) {
            value = data[i];
            for (j = i - 1; j >= 0; j--) {
                if (data[j] > value) {
                    SwapElement.swap(data, j + 1, j);
                } else {
                    break;
                }
            }
            data[j + 1] = value;
        }
    }
}
