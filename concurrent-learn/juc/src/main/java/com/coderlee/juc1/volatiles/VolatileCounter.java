package com.coderlee.juc1.volatiles;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VolatileCounter {
    private volatile int value;
    public int getValue() {
        return value;
    }
    public synchronized void increment() {
        value++;
    }

    public static void main(String[] args) {
        VolatileCounter counter = new VolatileCounter();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 1000; j++) {
                    counter.increment();
                }
            }, String.valueOf(i)).start();
        }
        SleepUtils.sleep(5*1000);
        log.info("counter.getValue() = {}", counter.getValue());
    }
}
