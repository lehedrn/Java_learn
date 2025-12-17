package com.coderlee.juc1.atomics;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * AtomicMarkableReference 原子标记引用演示类
 * 展示如何使用标记位解决CAS操作中的ABA问题
 * 通过布尔标记位跟踪引用是否被修改过，防止因值相同但实际发生变更的情况
 */
@Slf4j
public class AtomicMarkableReferenceDemo {
    // 创建一个AtomicMarkableReference实例，初始值为100，标记为false
    // AtomicMarkableReference通过维护一个引用值和一个布尔标记来检测变化
    static AtomicMarkableReference<Integer> atomicMarkableReference = new AtomicMarkableReference<>(100, false);

    public static void main(String[] args) {
        // 线程A：尝试修改引用值和标记位
        // 第一个参数是睡眠时间，第二个是期望值，第三个是新值
        new Thread(() -> changeValue(1000, 100, 2025), "A").start();

        // 线程B：也尝试修改引用值和标记位
        // 睡眠时间更长，确保在线程A操作后执行
        new Thread(() -> changeValue(2*1000, 100, 1991), "B").start();
    }

    /**
     * 修改AtomicMarkableReference值的通用方法
     * @param sleepTime 睡眠时间（毫秒）
     * @param expectedValue 期望的当前值
     * @param newValue 要设置的新值
     */
    public static void changeValue(long sleepTime, int expectedValue, int newValue) {
        // 获取当前标记状态，用于后续的CAS操作
        boolean marked = atomicMarkableReference.isMarked();
        log.info("{} 获取到的值: {}, 默认标记为: {}", Thread.currentThread().getName(),
                atomicMarkableReference.getReference(), marked);

        // 暂停指定时间，控制线程执行顺序
        SleepUtils.sleep(sleepTime);

        // 尝试原子性地将引用值从expectedValue更新为newValue，并翻转标记位
        // 只有当当前引用值等于expectedValue且标记位等于marked时才会更新成功
        boolean result = atomicMarkableReference.compareAndSet(expectedValue, newValue, marked, !marked);
        log.info("{} --------------- compareAndSet: {}, 获取到的值: {}, 当前标记为: {}",
                Thread.currentThread().getName(), result, atomicMarkableReference.getReference(),
                atomicMarkableReference.isMarked());
    }
}
