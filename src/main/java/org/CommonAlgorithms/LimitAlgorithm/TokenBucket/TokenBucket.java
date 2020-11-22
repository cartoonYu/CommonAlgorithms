package org.CommonAlgorithms.LimitAlgorithm.TokenBucket;

import java.util.concurrent.atomic.AtomicInteger;

public class TokenBucket {

    private int bucketCapacity;

    private AtomicInteger currentCapacity;

    public TokenBucket(int capacity, int produceRate){
        this.bucketCapacity = capacity;
        currentCapacity = new AtomicInteger(0);
        produceToken(produceRate);
    }

    public void produceToken(final int producerRate){
        new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(1000 / producerRate);
                } catch (InterruptedException e) {
                    System.out.println("thread sleep error, reason: " + e.getMessage());
                }
                if(currentCapacity.get() < bucketCapacity){
                    int produceTokenNum = currentCapacity.incrementAndGet();
                    System.out.println("token bucket algorithm, current capacity is: " + produceTokenNum);
                }
            }
        }).start();
    }

    public void consumeToken(TokenBucketFunction function, int consumeToken){
        if(currentCapacity.get() < consumeToken){
            System.out.println("token bucket algorithm, can't not access token");
            return;
        }
        currentCapacity.set(currentCapacity.intValue() - consumeToken);
        function.handle();
    }
}
