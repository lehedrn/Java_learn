package com.coderlee.juc1.objecthead;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectHeadDemo {
    public static void main(String[] args) {
        // new 一个对象，占内存多少  ---> 16 byte
        Object obj = new Object();
        // hashcode 记录在对象的什么地方
        log.info("{}", obj.hashCode());
        // 在哪里记录锁，锁几次
        synchronized (obj) {

        }
        // 手动垃圾回收
        System.gc();

        User c1 = new User();
        User c2 = new User();
        User c3 = new User();
    }
}



class User {
    int id;
    String name;
}