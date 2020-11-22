package org.CommonAlgorithms.LimitAlgorithm.Funnel;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Funnel {

    private int funnelCapacity;

    private AtomicInteger currentCapacity;

    private List<FunnelFunction> taskList;

    private List<Integer> consumeList;

    public Funnel(int capacity, int consumeRate){
        funnelCapacity = capacity;
        currentCapacity = new AtomicInteger(0);
        taskList = new LinkedList<>();
        consumeList = new LinkedList<>();
        consume(consumeRate);
    }

    private void consume(int consumeRate){
        new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(1000 / consumeRate);
                } catch (InterruptedException e) {
                    System.out.println("thread sleep error, reason: " + e.getMessage());
                }
                if(!taskList.isEmpty()){
                    taskList.get(0).handle();
                    currentCapacity.set(currentCapacity.get() - consumeList.get(0));
                }
            }
        }).start();
    }

    public void consume(FunnelFunction function, int consume){
        if(currentCapacity.get() + consume > funnelCapacity){
            System.out.println("funnel algorithm, can't not handle token");
            return;
        }
        currentCapacity.set(currentCapacity.intValue() + consume);
        taskList.add(function);
        consumeList.add(consume);
    }
}
