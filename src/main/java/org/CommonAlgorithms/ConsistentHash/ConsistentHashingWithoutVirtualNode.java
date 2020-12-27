package org.CommonAlgorithms.ConsistentHash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author cartoon
 * @date 2020/12/27
 */
public class ConsistentHashingWithoutVirtualNode {

    private static final Logger log = LoggerFactory.getLogger(ConsistentHashingWithoutVirtualNode.class);

    private SortedMap<Integer, String> hashToServers;

    private Map<String, List<String>> serverToData;

    public ConsistentHashingWithoutVirtualNode(String... servers) {
        hashToServers = new TreeMap<>();
        serverToData = new HashMap<>();
        for(String server : servers){
            hashToServers.put(getHash(server), server);
            serverToData.put(server, new LinkedList<>());
        }
        hashToServers.forEach((hash, server) -> log.info("init servers, hash: {}, server: {}", hash, server));
    }

    public boolean putData(String data){
        if(hashToServers.isEmpty()){
            log.error("put data, usable server is empty, put data result is false");
            return false;
        }
        int currentHash = getHash(data);
        SortedMap<Integer, String> usableServers = hashToServers.tailMap(currentHash);
        String server = usableServers.isEmpty() ? hashToServers.get(hashToServers.firstKey()) : usableServers.get(usableServers.firstKey());
        List<String> dataList = serverToData.get(server);
        dataList.add(data);
        log.info("put data, data {} is placed to server {}, put data result is true", data, server);
        return true;
    }

    public boolean removeServer(String server){
        int removeServerHash = getHash(server);
        if(!hashToServers.containsKey(removeServerHash)){
            log.error("remove server, current server is not in server list, please check server ip");
            return false;
        }
        if(hashToServers.size() == 1){
            hashToServers.remove(removeServerHash);
            serverToData.remove(server);
            log.info("remove server, after remove, server list is empty");
            return true;
        }
        List<String> removeServerData = serverToData.get(server);
        removeServerData.forEach(this::putData);
        log.info("remove server, remove server {} success", server);
        return true;
    }

    public void printDataInServers(){
        serverToData.forEach((server, data) ->{
            log.info("server {} contains data {}", server, data);
        });
    }

    private Integer getHash(String sourceData){
        return Math.abs(sourceData.hashCode());
    }
}
