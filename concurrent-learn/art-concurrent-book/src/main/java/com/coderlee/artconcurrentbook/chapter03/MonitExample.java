package com.coderlee.artconcurrentbook.chapter03;

import lombok.extern.slf4j.Slf4j;

/**
 * 锁的释放-获取建立的happens-before关系
 */
@Slf4j
public class MonitExample {
    int a = 0;
    public synchronized void write() { // 1
        a ++; //2
    } //3
    public synchronized void reader() { // 4
        int i = a; // 5
    } // 6
}
