package com.coderlee.juc1.base;

import lombok.extern.slf4j.Slf4j;

/**
 * 从Thread.start开始
 * 详见 docs/juc1/01/Thread.start()源码分析.md
 */
@Slf4j
public class ThreadBaseDemo {
    /**
     * 演示基本的线程创建和启动过程
     */
    public static void main(String[] args) {
        // 创建一个名为"t1"的新线程，使用Lambda表达式定义线程执行逻辑
        Thread t1 = new Thread(() -> {
            // 记录线程工作日志，输出当前线程的名称
            log.info("{} is working", Thread.currentThread().getName());
        }, "t1");

        // 启动线程，使其进入就绪状态等待CPU调度执行
        t1.start();
    }
}
