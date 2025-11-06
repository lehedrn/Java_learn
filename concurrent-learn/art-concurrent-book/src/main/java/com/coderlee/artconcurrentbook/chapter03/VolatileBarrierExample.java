package com.coderlee.artconcurrentbook.chapter03;

import lombok.extern.slf4j.Slf4j;

/**
 * volatile内存栅栏
 */
@Slf4j
public class VolatileBarrierExample {
    int a;
    volatile int v1 = 1;
    volatile int v2 = 2;
    void readAndWrite() {
        int i = v1; //第一个volatile读
        int j = v2; // 第二个volatile读
        a = i + j; //普通写
        v1 = i + 1; // 第一个volatile写
        v2 = j * 2; //第二个 volatile写
    }
}
