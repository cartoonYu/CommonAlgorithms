package org.CommonAlgorithms.ConsistentHash;

import org.CommonAlgorithms.HashAlgorithm.HashService;
import java.util.List;

/**
 * consistentHashing interface
 * @author cartoon
 * @since  10/01/2021
 * @version 1.1
 */
public interface ConsistentHashing {

    /**
     * put data to hash loop
     * @param data data list
     * @return put result
     */
    boolean putData(List<String> data);

    /**
     * put data to hash loop
     * @param data data
     * @return put result
     */
    boolean putData(String data);

    /**
     * remove node from hash loop
     * @param node removing node
     * @return remove result
     */
    boolean removeNode(String node);

    /**
     * add node to hash loop
     * @param node adding node
     * @return add result
     */
    boolean addNode(String node);

    /**
     * inject hash method to hash loop
     * @param hashService hash method
     * @throws UnsupportedOperationException if loop already has node
     */
    void setHashMethod(HashService hashService) throws UnsupportedOperationException;

    /**
     * print all data in loop according ascending hash value with nodes
     */
    void printAllData();

    default int defaultGetHash(String data){
        int res = Integer.parseInt(data.substring(0, 1)) * 100;
        if(!data.contains("&")){
            return res;
        }
        for(int index = data.indexOf("&") + 1; index < data.length(); index++){
            res += data.charAt(index) - 48;
        }
        return res;
    }
}
