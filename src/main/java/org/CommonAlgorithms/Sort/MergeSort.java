package org.CommonAlgorithms.Sort;

public class MergeSort {

    /**
     * 1. About Complexity
     *     1.1 Best time Complexity is O(n log n)
     *     1.2 Worst time Complexity is O(n log n)
     *     1.3 Average time Complexity is O(n log n)
     *     1.2 Space Complexity is O(n)
     * 2. Stability
     *     2.1 stable
     * 3. how it run
     *     3.1 this algorithm is base on recursive partition
     *     3.2 recurse to divide to be sorted array to one element and n array
     *     3.3 set every element between start and end to temp array
     *     3.4 circulate current start to end,compare left element to right element in temp array and set element according to condition
     * 4. Q&A
     * @param data
     */
    public static void sort(int[] data) {
        int[] temp = new int[data.length];
        sort(data, temp, 0, data.length - 1);
    }

    private static void sort(int[] data, int[] temp, int start, int end) {
        if (start >= end) {
            return;
        }
        int mid = start + ((end - start) >> 1);
        sort(data, temp, start, mid);
        sort(data, temp, mid + 1, end);
        merge(data, temp, start, end, mid);
    }

    private static void merge(int[] data, int[] temp, int start, int end, int mid) {
        for (int i = start; i <= end; i++) {
            temp[i] = data[i];
        }
        int left = start;
        int right = mid + 1;
        for (int i = start; i <= end; i++) {
            if (left > mid) {
                data[i] = temp[right++];
            } else if (right > end) {
                data[i] = temp[left++];
            } else if (temp[right] < temp[left]) {
                data[i] = temp[right++];
            } else {
                data[i] = temp[left++];
            }
        }
    }
}
