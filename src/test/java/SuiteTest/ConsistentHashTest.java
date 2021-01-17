package SuiteTest;

import ConsistentHash.ConsistentHashingWithVirtualNodeTest;
import ConsistentHash.ConsistentHashingWithoutVirtualNodeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author cartoon
 * @date 2021/01/17
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ConsistentHashingWithVirtualNodeTest.class, ConsistentHashingWithoutVirtualNodeTest.class})
public class ConsistentHashTest {
}
