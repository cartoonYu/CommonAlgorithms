package BackTracking;

public class ZeroOnePackage {

    /**
     * 1.About Complexity
     *     1.1 Best time Complexity is O(n^2)
     *     1.2 Space Complexity is O(1)
     * 2.how it run
     *     2.1 define some integer to cache calculate result
     *         2.1.1 maxValue,current max value
     *         2.1.2 curWeight,current weight in package
     *         2.1.3 curValue,current value with curWeight
     *         2.1.4 curPosition,current position of current element
     *     2.2 every element have two condition
     *         2.2.1 add it
     *         2.2.2 not add it
     *         2.2.3 with two condition build the solution space tree
     *     2.3 set end condition
     *         2.3.1 curWeight>capacity
     *               2.3.1.1 when this condition is true,there have no sense to go though another element
     *               2.3.1.2 use curValue subtract with last element's value
     *               2.3.1.3 compare curValue with maxValue
     *         2.3.2 curWeight=capacity
     *               2.3.2.1 when this condition is true,there have no sense to go though another element
     *               2.3.2.3 compare curValue with maxValue
     *         2.3.3 curPosition=weight.length
     *               2.3.3.1 when this condition is true,it means we have go though all element in this branch
     *               2.3.3.2 compare curValue with maxValue
     *     2.4 add current element value and weight to curValue and curWeight(2.2.1)
     *     2.5 recurse to traverse next element
     *     2.6 subtract curValue and curWeight with current element value and weight(2.2.2)
     *     2.7 recurse to traverse next element
     * 3.Q&A
     * @param weight
     * @param value
     * @param capacity
     * @return
     */
    public int cal(int[] weight,int[] value,int capacity){
        backTracking(weight,0,value,0,0,capacity);
        return maxValue;
    }

    private int maxValue;

    private void backTracking(int[] weight,int curWeight,int[] value,int curValue,int curPosition,int capacity){
        if(curWeight>capacity){
            curValue-=value[curPosition-1];
            maxValue=maxValue<curValue?curValue:maxValue;
            return;
        }
        if(curPosition==weight.length||curWeight==capacity){
            maxValue=maxValue<curValue?curValue:maxValue;
            return;
        }
        System.out.println(curPosition+" "+curWeight+" "+curValue);
        curWeight+=weight[curPosition];
        curValue+=value[curPosition];
        backTracking(weight,curWeight,value,curValue,curPosition+1,capacity);
        curValue-=value[curPosition];
        curWeight-=weight[curPosition];
        backTracking(weight,curWeight,value,curValue,curPosition+1,capacity);
    }
}
