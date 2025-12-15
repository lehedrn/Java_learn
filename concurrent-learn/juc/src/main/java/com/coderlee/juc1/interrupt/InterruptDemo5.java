package com.coderlee.juc1.interrupt;

import lombok.extern.slf4j.Slf4j;

/**
 * 演示Thread.interrupted()方法特性的示例类
 *
 * 本类重点展示了Thread.interrupted()方法的关键特性：
 * 1. 检查当前线程是否被中断
 * 2. 具有状态清除副作用：首次调用后会清除中断标志位
 * 3. 连续调用的行为差异
 */
@Slf4j
public class InterruptDemo5 {
    public static void main(String[] args) {
        // 第一次检查中断状态：默认情况下主线程未被中断，返回false
        log.info("{} ----> {}", Thread.currentThread().getName(), Thread.interrupted());

        // 第二次检查中断状态：由于之前没有中断，仍然返回false
        log.info("{} ----> {}", Thread.currentThread().getName(), Thread.interrupted());

        // 记录即将发起中断请求
        log.info("{} ----> start invoke interrupt", Thread.currentThread().getName());

        // 对当前线程（主线程）发起中断请求，设置中断标志位为true
        Thread.currentThread().interrupt();

        // 记录中断请求已完成
        log.info("{} ----> end invoke interrupt", Thread.currentThread().getName());

        // 第一次检查中断状态：检测到中断标志位为true，返回true，同时清除中断标志位
        log.info("{} ----> {}", Thread.currentThread().getName(), Thread.interrupted());

        // 第二次检查中断状态：由于中断标志位已被清除，返回false
        log.info("{} ----> {}", Thread.currentThread().getName(), Thread.interrupted());
    }
}
