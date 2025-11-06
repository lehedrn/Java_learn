package com.coderlee.artconcurrentbook.chapter03;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VolatileFeaturesExample {
    //使用volatile声明64位的long型变量
    volatile long v1 = 0L;
    public void set (long v1){
        //单个volatile变量的写
        this.v1 = v1;
    }
    public void getAndIncrement() {
        //复合（多个）volatile变量的读/写
        v1 ++;
    }
    public long get() {
        //单个volatile变量的读
        return v1;
    }
}
