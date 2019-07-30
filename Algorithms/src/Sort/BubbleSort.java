package Sort;

import Sort.util.SwapElement;

public class BubbleSort {

    /**
     * 1.About Complexity
     *     1.1 Best time Complexity is O(n)
     *     1.2 Worst time Complexity is O(n^2)
     *     1.3 Average time Complexity is O(n^2)
     *     1.2 Space Complexity is O(1)
     * 2.Stability
     *     2.1 stable
     * 3.how it run
     *     3.1 use double circulation to sort array(i circulate to record bound of ordered set,j circulate to swap element)
     * 4.Q&A
     *     4.1 Q:Why you define a boolean variable to affect circulation?
     *         A:this boolean variable can affect circulation certainly,it is used to record swap operation is happened.
     *            if there have no swap operation in a circulate,that means all element are sorted,so it is no mean to continue circulate,
     *            so it can end
     * @param data
     */
    public static void bubble(int[] data){
        int length=data.length;
        for(int i=0;i<length;i++){
            boolean flag=true;
            for(int j=0;j<length-1-i;j++){
                if(data[j]>data[j+1]){
                    SwapElement.swap(data,j,j+1);
                    flag=false;
                }
            }
            if(flag){
                return;
            }
        }
    }

}
