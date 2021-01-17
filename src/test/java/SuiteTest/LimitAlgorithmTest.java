package SuiteTest;

import LimitAlgorithm.FunnelTest;
import LimitAlgorithm.TokenBucketTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author cartoon
 * @date 2021/01/17
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({FunnelTest.class, TokenBucketTest.class})
public class LimitAlgorithmTest {
}
