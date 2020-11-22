package org.CommonAlgorithms.DynamicPlanning;

/**
 * Problem
 *     01package
 * Related solution
 *
 * @author cartoon
 * @version 1.0
 */
public class ZeroOnePackage {

    /**
     * 1.About Complexity
     *     1.1 Best time Complexity is O()
     *     1.2 Space Complexity is O(n*m)(n is weight.length,m is capacity)
     * 2.how it run
     *     2.1 define a array which length is n*m to record value result
     *     2.2 use double circulation to calculate result(it is similar with fill form with classic solution),there have five condition
     *         2.2.1 i=0&&j=0,it means current package capacity is 0,so res[i][j] must be 0
     *         2.2.2 i=0,it means only weight[0] to be chosen
     *               2.2.2.1 j(current package capacity)>=weight[i],res[i][j]=value[i]
     *               2.2.2.2 j(current package capacity)<weight[i],res[i][j]=0
     *         2.2.3 j=0,it means current package capacity is 0,res[i][j]=0
     *         2.2.4 j<weight[i],it means current package capacity is smaller than current weight,res[i][j]=res[i-1][j]
     *         2.2.5 j>=weight[i],it means current package capacity is bigger than current weight
     *               2.2.5.1 not add current element,res[i][j]=res[i-1][j]
     *               2.2.5.2 add current element,res[i][j]=res[i-1][j-weight[i]]+value[i],cause we need to clear package util it has enough space to add current element
     * 3.Q&A
     * @param weight
     * @param value
     * @param capacity
     * @return
     */
    public int cal(int[] weight, int[] value, int capacity) {
        int[][] res = new int[weight.length][capacity + 1];
        for (int i = 0, length = weight.length; i < length; i++) {
            for (int j = 0, size = capacity + 1; j < size; j++) {
                if (i == 0 && j == 0) {
                    res[i][j] = 0;
                } else if (i == 0) {
                    res[i][j] = j < weight[i] ? 0 : value[i];
                } else if (j == 0) {
                    res[i][j] = 0;
                } else if (j < weight[i]) {
                    res[i][j] = res[i - 1][j];
                } else {
                    res[i][j] = Math.max(res[i - 1][j], res[i - 1][j - weight[i]] + value[i]);
                }
            }
        }
        return res[weight.length - 1][capacity];
    }
}
