package com.coderlee.juc1.base;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadBaseDemo {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            log.info("{} is working", Thread.currentThread().getName());
        }, "t1");
        t1.start();
    }
}
