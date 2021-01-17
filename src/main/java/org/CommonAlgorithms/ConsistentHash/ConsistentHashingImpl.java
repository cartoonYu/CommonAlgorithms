package org.CommonAlgorithms.ConsistentHash;

import org.CommonAlgorithms.HashAlgorithm.HashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author cartoon
 * @date 2021/01/17
 */
public class ConsistentHashingImpl implements ConsistentHashing {

    private static final Logger log = LoggerFactory.getLogger(ConsistentHashingImpl.class);

    private static final String virtualNodeFormat = "%s&&%d";

    private SortedMap<String, List<String>> realNodeToVirtualNode;

    private SortedMap<Integer, String> hashToServers;

    private Map<String, List<String>> serverToData;

    private int virtualNodeNum;

    private HashService hashService;

    public ConsistentHashingImpl() {
        this(0, new String[0]);
    }

    public ConsistentHashingImpl(String... servers) {
        this(0, servers);
    }

    public ConsistentHashingImpl(int virtualNodeNum) {
        this(virtualNodeNum, new String[0]);
    }

    public ConsistentHashingImpl(int virtualNodeNum, String... servers) {
        if(virtualNodeNum < 0){
            log.error("virtual num is not allow smaller than 0");
            throw new IllegalArgumentException();
        }
        this.virtualNodeNum = virtualNodeNum;
        realNodeToVirtualNode = new TreeMap<>();
        hashToServers = new TreeMap<>();
        serverToData = new HashMap<>();
        for(String server : servers){
            hashToServers.put(getHash(server), server);
            serverToData.put(server, new LinkedList<>());
        }
        for(String server : servers){
            addVirtualNode(server);
        }
    }

    @Override
    public boolean putData(List<String> data) {
        for(String incomingData : data){
            if(!putData(incomingData)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean putData(String data) {
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

    @Override
    public boolean removeServer(String server) {
        int removeServerHash = getHash(server);
        if(!hashToServers.containsKey(removeServerHash)){
            log.error("remove server, current server is not in server list, please check server ip");
            return false;
        }
        List<String> removeServerData = serverToData.get(server);
        if(virtualNodeNum != 0){
            for(String virtualNode : realNodeToVirtualNode.get(server)){
                removeServerData.addAll(serverToData.get(virtualNode));
                hashToServers.remove(getHash(virtualNode));
                serverToData.remove(virtualNode);
            }
        }
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

    @Override
    public boolean addServer(String server) {
        int addServerHash = getHash(server);
        if(hashToServers.isEmpty()){
            hashToServers.put(addServerHash, server);
            serverToData.put(server, new LinkedList<>());
            if(virtualNodeNum != 0){
                addVirtualNode(server);
            }
        } else{
            SortedMap<Integer, String> greatServers = hashToServers.tailMap(addServerHash);
            String greatServer = greatServers.isEmpty() ? hashToServers.get(hashToServers.firstKey()) : greatServers.get(greatServers.firstKey());
            List<String> firstGreatServerData = new LinkedList<>(serverToData.get(greatServer));
            hashToServers.put(addServerHash, server);
            serverToData.put(greatServer, new LinkedList<>());
            serverToData.put(server, new LinkedList<>());
            if(virtualNodeNum != 0){
                addVirtualNode(server);
            }
            putData(firstGreatServerData);
        }
        log.info("add server, server {} has been added", server);
        return true;
    }

    @Override
    public void printDataInServers() {
        serverToData.forEach((server, data) -> log.info("server {} contains data {}", server, data));
    }

    @Override
    public void setHashMethod(HashService hashService) {
        this.hashService = hashService;
    }

    private void addVirtualNode(String server){
        if(virtualNodeNum > 0){
            List<String> currentVirtualNodeList = new LinkedList<>();
            for(int cnt = 0; cnt < this.virtualNodeNum; cnt++){
                String currentVirtualServerName = String.format(virtualNodeFormat, server, cnt);
                currentVirtualNodeList.add(currentVirtualServerName);
                int currentVirtualNodeHash = getHash(currentVirtualServerName);
                if(hashToServers.containsKey(currentVirtualNodeHash)){
                    break;
                }
                hashToServers.put(getHash(currentVirtualServerName), currentVirtualServerName);
                serverToData.put(currentVirtualServerName, new LinkedList<>());
            }
            realNodeToVirtualNode.put(server, currentVirtualNodeList);
        }
    }


    private int getHash(String server){
        return hashService == null ? defaultGetHash(server) : hashService.getHash(server);
    }

    private int defaultGetHash(String server){
        int res = 0;
        for(char tempChar : server.toCharArray()){
            if(tempChar >= '0' && tempChar <= '9'){
                res += tempChar;
            }
        }
        return res;
    }
}
