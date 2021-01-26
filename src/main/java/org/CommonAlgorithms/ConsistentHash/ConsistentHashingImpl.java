package org.CommonAlgorithms.ConsistentHash;

import org.CommonAlgorithms.HashAlgorithm.HashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * consistent hash achieve
 * @author cartoon
 * @since  2021/01/17
 */
public class ConsistentHashingImpl implements ConsistentHashing {

    private static final Logger log = LoggerFactory.getLogger(ConsistentHashingImpl.class);

    /**
     * virtual node name template
     */
    private static final String virtualNodeFormat = "%s&&%d";

    /**
     * real node and its virtual node mapping
     */
    private SortedMap<String, List<String>> realNodeToVirtualNode;

    /**
     * hash and its node mapping
     */
    private SortedMap<Integer, String> hashToNodes;

    /**
     * node and its data mapping
     */
    private Map<String, List<String>> nodeToData;

    /**
     * determine virtual node's number of each node
     */
    private int virtualNodeNum;

    /**
     * inject hash method, if null, use loop default hash method
     */
    private HashService hashService;


    public ConsistentHashingImpl() {
        this(0, new String[0]);
    }

    public ConsistentHashingImpl(String... nodes) {
        this(0, nodes);
    }

    public ConsistentHashingImpl(int virtualNodeNum) {
        this(virtualNodeNum, new String[0]);
    }

    public ConsistentHashingImpl(int virtualNodeNum, String... nodes) {
        //1. intercept virtual num smaller than 0
        if(virtualNodeNum < 0){
            log.error("virtual num is not allow smaller than 0");
            throw new IllegalArgumentException();
        }
        //2. initialize loop member attributes
        this.virtualNodeNum = virtualNodeNum;
        realNodeToVirtualNode = new TreeMap<>();
        hashToNodes = new TreeMap<>();
        nodeToData = new HashMap<>();
        for(String server : nodes){
            hashToNodes.put(getHash(server), server);
            nodeToData.put(server, new LinkedList<>());
        }
        //3. if virtual node number bigger than 0, add virtual node
        if(virtualNodeNum > 0){
            for(String server : nodes){
                addVirtualNode(server);
            }
        }
    }

    @Override
    public boolean putData(List<String> data) {
        //1. circulate call put data method to add data to loop
        for(String incomingData : data){
            if(!putData(incomingData)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean putData(String data) {
        if(hashToNodes.isEmpty()){
            log.error("put data, usable server is empty");
            return false;
        }
        //1. calculate data's hash value
        int currentHash = getHash(data);
        //2. get usual node(node's hash value is bigger than data's hash value), if usual node list is empty, get first node in loop
        SortedMap<Integer, String> usableNodes = hashToNodes.tailMap(currentHash);
        String node = usableNodes.isEmpty() ? hashToNodes.get(hashToNodes.firstKey()) : usableNodes.get(usableNodes.firstKey());
        //3. add data to node
        List<String> dataList = nodeToData.get(node);
        dataList.add(data);
        log.info("put data, data {} is placed to server {}, hash: {}", data, node, currentHash);
        return true;
    }

    @Override
    public boolean removeNode(String node) {
        //1. calculate hash value of removing node
        int removeServerHash = getHash(node);
        if(!hashToNodes.containsKey(removeServerHash)){
            log.error("remove server, current server is not in server list, please check server ip");
            return false;
        }
        //2. get data from removing node
        List<String> removeServerData = nodeToData.get(node);
        //3. get removing node's virtual node data, remove all virtual node with removing node
        if(virtualNodeNum != 0){
            for(String virtualNode : realNodeToVirtualNode.get(node)){
                removeServerData.addAll(nodeToData.get(virtualNode));
                hashToNodes.remove(getHash(virtualNode));
                nodeToData.remove(virtualNode);
            }
        }
        //4. remove node from hash loop
        hashToNodes.remove(removeServerHash);
        nodeToData.remove(node);
        if(hashToNodes.size() == 0){
            log.info("remove server, after remove, server list is empty");
            return true;
        }
        //5. put data to loop by call put data method
        putData(removeServerData);
        log.info("remove server, remove server {} success", node);
        return true;
    }

    @Override
    public boolean addNode(String node) {
        //1, calculate adding node's hash value
        int addServerHash = getHash(node);
        //2. add node and migrate data
        if(hashToNodes.isEmpty()){
            //2.1 add node and its virtual node to loop directly when current loop is empty
            hashToNodes.put(addServerHash, node);
            nodeToData.put(node, new LinkedList<>());
            if(virtualNodeNum > 0){
                addVirtualNode(node);
            }
        } else{
            //2.2.1 get data to be migrated from loop
            SortedMap<Integer, String> greatServers = hashToNodes.tailMap(addServerHash);
            String greatServer = greatServers.isEmpty() ? hashToNodes.get(hashToNodes.firstKey()) : greatServers.get(greatServers.firstKey());
            List<String> firstGreatServerData = new LinkedList<>(nodeToData.get(greatServer));
            //2.2.2 add node and its virtual node to loop
            hashToNodes.put(addServerHash, node);
            nodeToData.put(greatServer, new LinkedList<>());
            nodeToData.put(node, new LinkedList<>());
            if(virtualNodeNum != 0){
                addVirtualNode(node);
            }
            //2.2.3 migrate 2.2.1 data to loop by call put data method
            putData(firstGreatServerData);
        }
        log.info("add server, server {} has been added", node);
        return true;
    }

    @Override
    public void printAllData() {
        nodeToData.forEach((server, data) -> log.info("server {} contains data {}", server, data));
    }

    @Override
    public void setHashMethod(HashService hashService) {
        if(!hashToNodes.isEmpty()){
            throw new UnsupportedOperationException();
        }
        this.hashService = hashService;
    }

    private void addVirtualNode(String realNode){
        if(virtualNodeNum > 0){
            List<String> virtualNodeList = new LinkedList<>();
            for(int cnt = 0; cnt < this.virtualNodeNum; cnt++){
                //1. generate virtual node name by default format
                String virtualNodeName = String.format(virtualNodeFormat, realNode, cnt);
                //2. calculate each virtual node's hash value
                int virtualNodeHash = getHash(virtualNodeName);
                //3. current node already exist in loop, continue
                if(hashToNodes.containsKey(virtualNodeHash)){
                    continue;
                }
                //4. add node to loop
                virtualNodeList.add(virtualNodeName);
                hashToNodes.put(virtualNodeHash, virtualNodeName);
                nodeToData.put(virtualNodeName, new LinkedList<>());
            }
            //5. map virtual node to real node
            realNodeToVirtualNode.put(realNode, virtualNodeList);
        }
    }


    private int getHash(String data){
        return hashService == null ? defaultGetHash(data) : hashService.getHash(data);
    }

    private int defaultGetHash(String data){
        int res = 0;
        for(char tempChar : data.toCharArray()){
            if(tempChar >= '0' && tempChar <= '9'){
                res += tempChar;
            }
        }
        return res;
    }
}
