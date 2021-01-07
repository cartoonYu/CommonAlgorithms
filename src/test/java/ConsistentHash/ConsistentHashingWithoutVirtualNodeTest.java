package ConsistentHash;

import org.CommonAlgorithms.ConsistentHash.ConsistentHashingWithoutVirtualNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cartoon
 * @date 2020/12/27
 */
public class ConsistentHashingWithoutVirtualNodeTest {

    private static final Logger log = LoggerFactory.getLogger(ConsistentHashingWithoutVirtualNodeTest.class);

    private ConsistentHashingWithoutVirtualNode consistentHashingWithoutVirtualNode;

    private String[] servers;

    private String[] data;

    @Before
    public void before(){
        servers = new String[]{"000", "111", "222", "333", "555"};
        consistentHashingWithoutVirtualNode = new ConsistentHashingWithoutVirtualNode(servers);
        data = new String[]{"000", "111", "222", "333", "555"};
    }

    @Test
    public void testConsistentHashing(){
        for(String str : data){
            Assert.assertTrue(consistentHashingWithoutVirtualNode.putData(str));
        }
        consistentHashingWithoutVirtualNode.removeServer("333");
        consistentHashingWithoutVirtualNode.addServer("444");
        consistentHashingWithoutVirtualNode.putData("444");
        consistentHashingWithoutVirtualNode.printDataInServers();
    }
}
