package ConsistentHash;

import org.CommonAlgorithms.ConsistentHash.ConsistentHashingWithoutVirtualNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author cartoon
 * @date 2020/12/27
 */
public class ConsistentHashingWithoutVirtualNodeTest {

    private ConsistentHashingWithoutVirtualNode consistentHashingWithoutVirtualNode;

    private String[] servers;

    private String[] data;

    @Before
    public void before(){
        servers = new String[]{"192.168.0.0:111", "192.168.0.1:111",
                "192.168.0.2:111", "192.168.0.3:111", "192.168.0.4:111"};
        consistentHashingWithoutVirtualNode = new ConsistentHashingWithoutVirtualNode(servers);
        data = new String[]{"192.168.0.0:111", "192.168.0.1:111",
                "192.168.0.2:111", "192.168.0.3:111", "192.168.0.3:111"};
    }

    @Test
    public void testConsistentHashing(){
        for(String str : data){
            Assert.assertTrue(consistentHashingWithoutVirtualNode.putData(str));
        }
        consistentHashingWithoutVirtualNode.printDataInServers();
        consistentHashingWithoutVirtualNode.removeServer("192.168.0.3:111");
        consistentHashingWithoutVirtualNode.addServer("192.168.0.5:111");
    }
}
