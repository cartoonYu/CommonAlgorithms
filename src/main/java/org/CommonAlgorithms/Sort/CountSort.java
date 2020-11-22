package org.CommonAlgorithms.Sort;

public class CountSort {

    /**
     * 1. About Complexity
     *     1.1 Best time Complexity is O(n)
     *     1.2 Worst time Complexity is O(n)
     *     1.3 Average time Complexity is O(n)
     *     1.2 Space Complexity is O(n)
     * 2. Stability
     *     2.1 unstable
     * 3. how it run
     *     3.1 get num and its appearance time from array and form a array
     *     3.2 use 3.1 array to reset original array
     * 4. Q&A
     * @param data
     */
    public static void sort(int[] data) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int length = data.length;
        for (int i = 0; i < length; i++) {
            max = max < data[i] ? data[i] : max;
            min = min > data[i] ? data[i] : min;
        }
        int[] temp = new int[max - min + 1];
        for (int i = 0; i < length; i++) {
            temp[data[i] - min]++;
        }
        int index = length - 1, flag = max;
        for (int i = temp.length - 1; i >= 0; i--) {
            for (int j = temp[i]; j > 0; j--) {
                data[index--] = flag;
            }
            flag--;
        }
    }

}
