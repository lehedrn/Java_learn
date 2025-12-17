package com.coderlee.juc1.atomics;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * AtomicReferenceFieldUpdater 原子引用字段更新器演示类
 * 展示如何使用AtomicReferenceFieldUpdater实现单例模式的线程安全初始化
 */
@Slf4j
public class AtomicReferenceFieldUpdaterDemo {
    /**
     * 主方法，创建MyVar实例并启动多个线程同时尝试初始化
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 创建MyVar实例
        MyVar myVar = new MyVar();
        // 启动5个线程同时尝试初始化操作
        for (int i = 0; i < 5; i++) {
            new Thread(myVar::initConstruct, String.valueOf(i)).start();
        }
    }
}

/**
 * 模拟需要单次初始化的类
 * 使用AtomicReferenceFieldUpdater确保初始化操作的线程安全性
 */
@Slf4j
class MyVar {
    // 初始化状态标志，volatile确保多线程间的可见性
    public volatile Boolean init = Boolean.FALSE;

    // 原子引用字段更新器，用于对init字段进行原子操作
    // 声明为static final以提高性能
    static final AtomicReferenceFieldUpdater<MyVar, Boolean> updater =
            AtomicReferenceFieldUpdater.newUpdater(MyVar.class, Boolean.class, "init");

    /**
     * 初始化构造方法，确保只被一个线程执行
     * 使用CAS操作保证只有一个线程能成功将init从FALSE更新为TRUE
     */
    public void initConstruct() {
        // 使用compareAndSet进行原子比较和设置操作
        // 只有当init字段当前值为FALSE时，才能成功更新为TRUE
        if (updater.compareAndSet(this, Boolean.FALSE, Boolean.TRUE)) {
            // 成功获取初始化权限的线程执行初始化逻辑
            log.info("{} -----------------start init, need 2 seconds", Thread.currentThread().getName());
            // 模拟耗时的初始化操作
            SleepUtils.sleep(2 * 1000);
            log.info("{} -----------------over init", Thread.currentThread().getName());
        } else {
            // 其他线程发现已经被初始化，直接返回
            log.info("{} ----------------- has other thread doing init", Thread.currentThread().getName());
        }
    }
}
