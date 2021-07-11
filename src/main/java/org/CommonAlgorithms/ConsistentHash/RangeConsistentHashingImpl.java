package org.CommonAlgorithms.ConsistentHash;

import org.CommonAlgorithms.HashAlgorithm.HashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author cartoon
 * @version 1.0
 * @since 2021/6/20 16:22
 */
public class RangeConsistentHashingImpl implements ConsistentHashing{

    private static final Logger log = LoggerFactory.getLogger(ConsistentHashing.class);

    /**
     * virtual node name template
     */
    private static final String virtualNodeFormat = "%s&%d";


    /**
     * real node and its virtual node mapping
     */
    private SortedMap<String, List<String>> realNodeToVirtualNode;

    /**
     * hash and its node mapping
     */
    private SortedMap<Integer, String> hashToNodes;

    private List<NodeToData> nodeToDataList;

    /**
     * determine virtual node's number of each node
     */
    private int virtualNodeNum;

    /**
     * inject hash method, if null, use loop default hash method
     */
    private HashService hashService;

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

        List<NodeToData> usableNodeList = nodeToDataList.stream()
                                            .filter(nodeToData -> nodeToData.getStartRange() <= currentHash && nodeToData.getEndRange() > currentHash)
                                            .collect(Collectors.toList());
        NodeToData usableNode = usableNodeList.isEmpty() ? nodeToDataList.stream()
                .min(Comparator.comparing(NodeToData::getStartRange)).get() : usableNodeList.get(0);
        usableNode.getData().add(data);
        log.info("put data, data {} is placed to server {}, hash: {}", data, usableNode.getRealNodeName(), currentHash);
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
        List<NodeToData> removeServerData = nodeToDataList.stream()
                .filter(nodeToData -> nodeToData.getRealNodeHash().equals(removeServerHash))
                .collect(Collectors.toList());
        //3. get removing node's virtual node data, remove all virtual node with removing node
        if(virtualNodeNum != 0){
            for(String virtualNode : realNodeToVirtualNode.get(node)){
                hashToNodes.remove(getHash(virtualNode));
            }
        }
        //4. remove node from hash loop
        hashToNodes.remove(removeServerHash);
        nodeToDataList.removeAll(removeServerData);
        if(hashToNodes.size() == 0){
            log.info("remove server, after remove, server list is empty");
            return true;
        }
        //5. put data to loop by call put data method
        List<String> toReaddData = new LinkedList<>();
        removeServerData.stream().map(NodeToData::getData).collect(Collectors.toList()).forEach(toReaddData::addAll);
        putData(toReaddData);
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
            nodeToDataList.add(new NodeToData(node, addServerHash, null, addServerHash, addServerHash, addServerHash));
            if(virtualNodeNum > 0){
                addVirtualNode(node, addServerHash);
            }
        } else{
            //2.2.1 get data to be migrated from loop
            SortedMap<Integer, String> greatServers = hashToNodes.tailMap(addServerHash);
            Integer greatServerHash = greatServers.isEmpty() ? hashToNodes.firstKey() : greatServers.firstKey();
            nodeToDataList.add(new NodeToData(node, addServerHash, null, addServerHash, greatServerHash, addServerHash));
            List<String> firstGreatServerData = new LinkedList<>();
            nodeToDataList.stream().filter(nodeToData -> nodeToData.getNodeHash().equals(greatServerHash)).map(NodeToData::getData).collect(Collectors.toList()).forEach(firstGreatServerData::addAll);

            if(virtualNodeNum != 0){
                addVirtualNode(node, addServerHash);
            }
            //2.2.3 migrate 2.2.1 data to loop by call put data method
            putData(firstGreatServerData);
        }
        log.info("add server, server {} has been added", node);
        return true;
    }

    @Override
    public void setHashMethod(HashService hashService) throws UnsupportedOperationException {
        if(!hashToNodes.isEmpty()){
            throw new UnsupportedOperationException();
        }
        this.hashService = hashService;
    }

    @Override
    public void printAllData() {
        nodeToDataList.forEach(nodeToData -> log.info("server {}, virtual node {}, start range {}, end range {}, contains data {}", nodeToData.getRealNodeName(), nodeToData.getVirtualNodeName(), nodeToData.getStartRange(), nodeToData.getEndRange(), String.join(",", nodeToData.getData())));
    }

    private void addVirtualNode(String realNode, Integer realNodeHash){
        if(virtualNodeNum > 0){
            SortedSet<Integer> virtualNodeHashSet = new TreeSet<>();
            virtualNodeHashSet.add(realNodeHash);
            List<String> virtualNodeList = new LinkedList<>();
            for(int cnt = 1; cnt <= this.virtualNodeNum; cnt++){
                //1. generate virtual node name by default format
                String virtualNodeName = String.format(virtualNodeFormat, realNode, cnt);
                //2. calculate each virtual node's hash value
                int virtualNodeHash = getHash(virtualNodeName);
                int smallerThanVirtualNodeHashHash = virtualNodeHashSet.headSet(virtualNodeHash).last();
                //3. current node already exist in loop, continue
                if(hashToNodes.containsKey(virtualNodeHash)){
                    continue;
                }
                //4. add node to loop
                virtualNodeList.add(virtualNodeName);
                hashToNodes.put(virtualNodeHash, virtualNodeName);
                nodeToDataList.add(new NodeToData(realNode, realNodeHash, virtualNodeName, virtualNodeHash, smallerThanVirtualNodeHashHash, virtualNodeHash));
                virtualNodeHashSet.add(virtualNodeHash);
            }
            //5. map virtual node to real node
            realNodeToVirtualNode.put(realNode, virtualNodeList);
        }
    }

    private int getHash(String data){
        return hashService == null ? defaultGetHash(data) : hashService.getHash(data);
    }

    public RangeConsistentHashingImpl() {
        this(0, new String[0]);
    }

    public RangeConsistentHashingImpl(String... nodes) {
        this(0, nodes);
    }

    public RangeConsistentHashingImpl(int virtualNodeNum) {
        this(virtualNodeNum, new String[0]);
    }

    public RangeConsistentHashingImpl(int virtualNodeNum, String... nodes) {
        //1. intercept virtual num smaller than 0
        if(virtualNodeNum < 0){
            log.error("virtual num is not allow smaller than 0");
            throw new IllegalArgumentException();
        }
        //2. initialize loop member attributes
        this.virtualNodeNum = virtualNodeNum;
        realNodeToVirtualNode = new TreeMap<>();
        hashToNodes = new TreeMap<>();
        nodeToDataList = new LinkedList<>();
        for(String server : nodes){
            int realNodeHash = getHash(server);
            hashToNodes.put(realNodeHash, server);
            if(nodeToDataList.isEmpty()){
                nodeToDataList.add(new NodeToData(server, realNodeHash, null, realNodeHash, realNodeHash, realNodeHash));
            } else {
                int currentDataListBiggestHash = nodeToDataList.stream()
                        .filter(nodeToData -> nodeToData.getEndRange() <= realNodeHash)
                        .max(Comparator.comparing(NodeToData::getEndRange))
                        .get()
                        .getEndRange();
                nodeToDataList.add(new NodeToData(server, realNodeHash, null, realNodeHash, currentDataListBiggestHash, realNodeHash));
            }

            addVirtualNode(server, realNodeHash);
        }
    }

    class NodeToData{

        private String realNodeName;

        private Integer realNodeHash;

        private Integer nodeHash;

        private String virtualNodeName;

        private Integer startRange;

        private Integer endRange;

        private List<String> data;

        public NodeToData(String realNodeName, Integer realNodeHash,
                          String virtualNodeName, Integer nodeHash,
                          Integer startRange, Integer endRange) {
            this.realNodeName = realNodeName;
            this.startRange = startRange;
            this.endRange = endRange;
            this.nodeHash = nodeHash;
            this.realNodeHash = realNodeHash;
            this.virtualNodeName = virtualNodeName;
            data = new LinkedList<>();
        }

        public Integer getStartRange() {
            return startRange;
        }

        public NodeToData setStartRange(Integer startRange) {
            this.startRange = startRange;
            return this;
        }

        public Integer getEndRange() {
            return endRange;
        }

        public NodeToData setEndRange(Integer endRange) {
            this.endRange = endRange;
            return this;
        }

        public List<String> getData() {
            return data;
        }

        public NodeToData setData(List<String> data) {
            this.data = data;
            return this;
        }

        public String getRealNodeName() {
            return realNodeName;
        }

        public NodeToData setRealNodeName(String realNodeName) {
            this.realNodeName = realNodeName;
            return this;
        }

        public Integer getNodeHash() {
            return nodeHash;
        }

        public NodeToData setNodeHash(Integer nodeHash) {
            this.nodeHash = nodeHash;
            return this;
        }

        public String getVirtualNodeName() {
            return virtualNodeName;
        }

        public NodeToData setVirtualNodeName(String virtualNodeName) {
            this.virtualNodeName = virtualNodeName;
            return this;
        }

        public Integer getRealNodeHash() {
            return realNodeHash;
        }

        public NodeToData setRealNodeHash(Integer realNodeHash) {
            this.realNodeHash = realNodeHash;
            return this;
        }
    }
}
