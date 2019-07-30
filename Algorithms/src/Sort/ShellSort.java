package Sort;

import Sort.util.SwapElement;

public class ShellSort {

    /**
     * 1. About Complexity
     *     1.1 Best time Complexity is O(n)
     *     1.2 Worst time Complexity is O(n^2)
     *     1.3 Average time Complexity is O(n^1.2-n^1.5)
     *     1.2 Space Complexity is O(1)
     * 2. Stability
     *     2.1 unstable
     * 3. how it run
     *     3.1 calculate section first
     *     3.2 use triple circulation to sort element
     *          3.2.1 first circulation is use section as condition,when section is 0,elements are sorted
     *          3.2.2 second circulation to loop section to array length,record current element
     *          3.2.3 third circulation to compare current element to record value and swap
     * 4. Q&A
     * @param data
     */
    public static void sort(int[] data){
        if(data.length<2){
            return;
        }
        int length=data.length;
        int gap=1;
        while (gap<length){
            gap=gap*3+1;
        }
        int temp,j;
        while (gap>0){
            for(int i=gap;i<length;i++){
                temp=data[i];
                j=i-gap;
                while (j>=0&&data[j]>temp){
                    SwapElement.swap(data,gap+j,j);
                    j-=gap;
                }
                data[j+gap]=temp;
            }
            gap/=3;
        }
    }
}
