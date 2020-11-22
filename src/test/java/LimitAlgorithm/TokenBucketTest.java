package LimitAlgorithm;

import org.CommonAlgorithms.LimitAlgorithm.TokenBucket.TokenBucket;
import org.junit.Before;
import org.junit.Test;


public class TokenBucketTest {

    private TokenBucket tokenBucket;

    @Before
    public void before(){
        tokenBucket = new TokenBucket(100, 5);
    }

    @Test
    public void testTokenBucket(){
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tokenBucket.consumeToken(()-> System.out.println("consume token"), 2);
        }
    }
}
