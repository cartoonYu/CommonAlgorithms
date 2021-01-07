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

    public boolean putData(List<String> data){
        for(String incomingData : data){
            if(!putData(incomingData)){
                return false;
            }
        }
        return true;
    }

    public boolean putData(String data){
        if(hashToServers.isEmpty()){
            log.error("put data, usable server is empty");
            return false;
        }
        int currentHash = getHash(data);
        SortedMap<Integer, String> usableServers = hashToServers.tailMap(currentHash);
        String server = usableServers.isEmpty() ? hashToServers.get(hashToServers.firstKey()) : usableServers.get(usableServers.firstKey());
        List<String> dataList = serverToData.get(server);
        dataList.add(data);
        log.info("put data, data {} is placed to server {}, hash: {}", data, server, currentHash);
        return true;
    }

    public boolean removeServer(String server){
        int removeServerHash = getHash(server);
        if(!hashToServers.containsKey(removeServerHash)){
            log.error("remove server, current server is not in server list, please check server ip");
            return false;
        }
        List<String> removeServerData = serverToData.get(server);
        hashToServers.remove(removeServerHash);
        serverToData.remove(server);
        if(hashToServers.size() == 0){
            log.info("remove server, after remove, server list is empty");
            return true;
        }
        putData(removeServerData);
        log.info("remove server, remove server {} success", server);
        return true;
    }

    public boolean addServer(String server){
        int addServerHash = getHash(server);
        if(hashToServers.containsKey(addServerHash)){
            log.error("add server, server {} is already exist", server);
            return false;
        }
        if(hashToServers.isEmpty()){
            hashToServers.put(addServerHash, server);
            serverToData.put(server, new LinkedList<>());
        } else{
            SortedMap<Integer, String> greatServers = hashToServers.tailMap(addServerHash);
            String greatServer = greatServers.isEmpty() ? hashToServers.get(hashToServers.firstKey()) : greatServers.get(greatServers.firstKey());
            List<String> firstGreatServerData = new LinkedList<>(serverToData.get(greatServer));
            hashToServers.put(addServerHash, server);
            serverToData.put(server, new LinkedList<>());
            serverToData.put(greatServer, new LinkedList<>());
            putData(firstGreatServerData);
        }
        log.info("add server, server {} has been added", server);
        return true;
    }

    public void printDataInServers(){
        serverToData.forEach((server, data) -> log.info("server {} contains data {}", server, data));
    }

    private Integer getHash(String sourceData){
        int res = 0;
        for(char tempChar : sourceData.toCharArray()){
            if(tempChar >= '0' && tempChar <= '9'){
                res += tempChar;
            }
        }
        return res;
    }
}
