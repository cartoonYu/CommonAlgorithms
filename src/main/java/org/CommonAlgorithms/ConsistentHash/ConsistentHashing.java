package org.CommonAlgorithms.ConsistentHash;

import org.CommonAlgorithms.HashAlgorithm.HashService;

import java.util.List;

/**
 * @author cartoon
 * @date 10/01/2021
 */
public interface ConsistentHashing {

    boolean putData(List<String> data);

    boolean putData(String data);

    boolean removeServer(String server);

    boolean addServer(String server);

    void setHashMethod(HashService hashService);

    void printDataInServers();

}
