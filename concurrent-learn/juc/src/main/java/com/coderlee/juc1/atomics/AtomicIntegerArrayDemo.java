package com.coderlee.juc1.atomics;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * AtomicIntegerArray 原子整数数组操作演示类
 * 展示如何使用AtomicIntegerArray进行线程安全的数组操作
 */
@Slf4j
public class AtomicIntegerArrayDemo {
    public static void main(String[] args) {
        // 创建一个长度为5的原子整数数组，所有元素初始化为0
        // 注释掉的其他构造方式：
        // 1. new AtomicIntegerArray(5) - 创建指定长度的数组，元素默认为0
        // 2. new AtomicIntegerArray(new int[]{1, 2, 3, 4, 5}) - 使用给定数组创建
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[5]);

        // 输出数组长度
        log.info("atomicIntegerArray.length() = {}", atomicIntegerArray.length());

        // 遍历并打印数组中每个元素的值
        for (int i = 0; i < atomicIntegerArray.length(); i++) {
            log.info("atomicIntegerArray[{}] = {}", i, atomicIntegerArray.get(i));
        }

        // 用于存储操作前原始值的临时变量
        int tempInt = 0;

        // 原子性地设置索引0处的值为2025，并返回原来的值
        tempInt = atomicIntegerArray.getAndSet(0, 2025);
        log.info("atomicIntegerArray[0] = {}, 原始值: {}", atomicIntegerArray.get(0), tempInt);

        // 原子性地将索引0处的值递增1，并返回递增前的值
        tempInt = atomicIntegerArray.getAndIncrement(0);
        log.info("atomicIntegerArray[0] = {}, 原始值: {}", atomicIntegerArray.get(0), tempInt);
    }
}
