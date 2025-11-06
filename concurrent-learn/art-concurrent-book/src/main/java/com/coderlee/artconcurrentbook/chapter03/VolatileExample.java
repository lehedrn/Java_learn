package com.coderlee.artconcurrentbook.chapter03;

import lombok.extern.slf4j.Slf4j;

/**
 * volatile写-读建立的happens-before关系
 */
@Slf4j
public class VolatileExample {
    int a = 0;
    volatile boolean flag = false;
    public void write() {
        a = 1;
        flag = true;
    }
    public void read() {
        if (flag) {
            int i = a * a;
            log.info("Read: i={}", i);
        }
    }
}
