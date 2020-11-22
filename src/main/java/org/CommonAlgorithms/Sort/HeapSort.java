package org.CommonAlgorithms.Sort;

import org.CommonAlgorithms.Sort.util.SwapElement;

public class HeapSort {

    /**
     * 1. About Complexity
     *     1.1 Best time Complexity is O(n log n)
     *     1.2 Worst time Complexity is O(n log n)
     *     1.3 Average time Complexity is O(n log n)
     *     1.2 Space Complexity is O(1)
     * 2. Stability
     *     2.1 unstable
     * 3. how it run
     *     3.1 form a minimum heap or maximum heap with original array
     *     3.2 get the top element (array[0]) every time and self-balancing to 3.1 heap
     * 4. Q&A
     * @param data
     */
    public static void sort(int[] data) {
        buildHeap(data);
        int flag = data.length;
        for (int i = data.length - 1; i > 0; i--) {
            SwapElement.swap(data, 0, i);
            flag--;
            sink(data, 0, flag);
        }
    }

    private static void buildHeap(int[] data) {
        for (int i = data.length / 2; i >= 0; i--) {
            sink(data, i, data.length);
        }
    }


    private static void sink(int[] data, int index, int length) {
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        int current = index;
        if (left < length && data[left] < data[current]) {
            current = left;
        }
        if (right < length && data[right] < data[current]) {
            current = right;
        }
        if (current != index) {
            SwapElement.swap(data, current, index);
            sink(data, current, length);
        }
    }
}
