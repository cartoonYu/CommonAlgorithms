package ConsistentHash;

import org.CommonAlgorithms.ConsistentHash.ConsistentHashing;
import org.CommonAlgorithms.ConsistentHash.ConsistentHashingImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cartoon
 * @date 2021/01/17
 */
public class ConsistentHashingWithVirtualNodeTest {

    private static final Logger log = LoggerFactory.getLogger(ConsistentHashingWithVirtualNodeTest.class);

    private ConsistentHashing consistentHashing;

    private String[] servers;

    private String[] data;

    @Before
    public void before(){
        servers = new String[]{"000", "111", "222", "333", "555"};
        consistentHashing = new ConsistentHashingImpl(3, servers);
        data = new String[]{"000", "111", "222", "333", "555"};
    }

    @Test
    public void testConsistentHashing(){
        for(String str : data){
            Assert.assertTrue(consistentHashing.putData(str));
        }
        consistentHashing.removeNode("333");
        consistentHashing.addNode("444");
        consistentHashing.putData("444");
        consistentHashing.putData("555&&0");
        consistentHashing.printAllData();
    }
}
