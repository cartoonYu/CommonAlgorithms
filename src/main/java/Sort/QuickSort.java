package Sort;

import Sort.util.SwapElement;

public class QuickSort {

    /**
     * 1. About Complexity
     *     1.1 Best time Complexity is O(nlogn)
     *     1.2 Worst time Complexity is O(n^2)
     *     1.3 Average time Complexity is O(nlogn)
     *     1.2 Space Complexity is O(1)
     * 2. Stability
     *     2.1 unstable
     * 3. how it run
     *     3.1 this solution is base on recursive partition
     *     3.2 define two integer with start index and end index
     *     3.3 swap element of two pointer when data[i]>data[j]
     *     3.4 with 3.3,on the partition left side element are smaller than data[partition],on the partition right side element are bigger than data[partition]
     *     3.5 recurse left side and right side of partition apart
     * 4. Q&A
     * @param data
     */
    public static void sort(int[] data) {
        sort(data, 0, data.length - 1);
    }

    private static void sort(int data[], int start, int end) {
        if (start < end) {
            int partition = partition(data, start, end);
            sort(data, start, partition - 1);
            sort(data, partition + 1, end);
        }
    }

    private static int partition(int data[], int i, int j) {
        while (i < j) {
            while (i < j && data[i] < data[j]) {
                i++;
            }
            if (i < j) {
                SwapElement.swap(data, i, j);
            }
            while (i < j && data[i] < data[j]) {
                j--;
            }
            if (i < j) {
                SwapElement.swap(data, i, j);
            }
        }
        return i;
    }
}
