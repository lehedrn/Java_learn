package com.coderlee.juc1.locks;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LockSyncDemo {
    Object obj = new Object();

    // 一般情况:一个monitoreenter和两个monitorexit指令
    public void m1() {
        synchronized (obj) {
            log.info("----hello synchronized code block");
        }
    }

    // 极端情况:一个monitorenter和一个monitorexit指令
    public void m1_1() {
        synchronized (obj) {
            log.info("----hello synchronized code block");
            throw new RuntimeException("---exp");
        }
    }

    public synchronized void m2() {
        log.info("----hello synchronized method 2");
    }

    public static synchronized void m3() {
        log.info("----hello synchronized method 3");
    }

    public static void main(String[] args) {

    }
}
