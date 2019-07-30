package Sort;

import Sort.util.SwapElement;

public class SelectionSort {

    /**
     * 1. About Complexity
     *     1.1 Best time Complexity is O(n^2)
     *     1.2 Worst time Complexity is O(n^2)
     *     1.3 Average time Complexity is O(n^2)
     *     1.2 Space Complexity is O(1)
     * 2. Stability
     *     2.1 stable
     * 3. how it run
     *     3.1 use a double circulation to compare
     *             3.1.1 use a integer to mark the minimum element index
     *             3.1.2 circulate within of border to find minimum element
     *             3.1.3 swap element with current index and minimum
     * 4. Q&A
     * @param data
     */
    public static void sort(int[] data){
        if(data.length<2){
            return;
        }
        int index;
        for(int i=0,length=data.length;i<length;i++){
            index=i;
            for(int j=i+1;j<length;j++){
                if(data[j]<data[index]){
                    index=j;
                }
            }
            SwapElement.swap(data,i,index);
        }
    }
}
