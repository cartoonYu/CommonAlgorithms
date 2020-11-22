package LimitAlgorithm;

import org.CommonAlgorithms.LimitAlgorithm.Funnel.Funnel;
import org.junit.Before;
import org.junit.Test;

public class FunnelTest {

    private Funnel funnel;

    @Before
    public void before(){
        funnel = new Funnel(100, 1);
    }

    @Test
    public void testFunnel() throws InterruptedException {
        while (true){
            Thread.sleep(500);
            funnel.consume(()-> System.out.println("consume"), 40);
        }
    }
}
