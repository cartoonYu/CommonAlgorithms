package org.CommonAlgorithms.Sort.util;

public class SwapElement {

    /**
     * this function is used to swap element between index i and index j in array
     * @param data
     * @param i
     * @param j
     */
    public static void swap(int[] data, int i, int j) {
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

}
