package com.coderlee.designpattern.creational.singleton;

import com.coderlee.designpattern.creational.singleton.supports.SingletonTestUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 单例模式示例 03 - 懒汉式实现（线程不安全版本）
 * <p>
 * 特点：
 * 1. 延迟加载：只有在第一次调用 getInstance() 时才创建实例
 * 2. 线程不安全：多线程环境下可能创建多个实例
 * 3. 性能问题：存在竞态条件，不适合并发场景
 * </p>
 * <p>
 * 问题分析：
 * 当多个线程同时调用 getInstance() 时，如果 INSTANCE 为 null，
 * 多个线程可能同时通过 null 检查，导致创建多个实例
 * </p>
 *
 * @author coderlee
 * @version 1.0
 */
@Slf4j
public class SingletonExample03 {
    /**
     * 私有构造函数，防止外部通过 new 关键字创建实例
     * 确保只能通过 getInstance() 方法获取实例
     */
    private SingletonExample03() {}

    /**
     * 静态变量存储单例实例
     * volatile 关键字缺失导致线程不安全问题
     * null 表示初始状态，首次调用时创建实例
     */
    private static SingletonExample03 INSTANCE = null;

    /**
     * 获取单例实例的方法（线程不安全）
     * 采用懒加载方式，但存在线程安全隐患
     *
     * @return SingletonExample03 实例
     * @throws RuntimeException 多线程环境下可能创建多个实例
     */
    public static SingletonExample03 getInstance() {
        // 第一次检查：判断实例是否已创建
        if (INSTANCE == null) {
            // 模拟实例创建过程中的耗时操作
            // 这段代码增加了线程不安全问题的暴露概率
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 创建新实例（多线程环境下这里会创建多个实例）
            INSTANCE = new SingletonExample03();
        }
        // 返回创建的实例
        return INSTANCE;
    }

    /**
     * 主方法，用于测试单例模式的线程安全性
     * 重点测试多线程环境下是否能保证单例
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
//        SingletonTestUtil.testSingleton(SingletonExample03.class.getSimpleName(), SingletonExample03::getInstance);
//        SingletonTestUtil.testReflection(SingletonExample03.class.getSimpleName(), SingletonExample03.class, SingletonExample03::getInstance);
        // 需要先把上面验证注释掉，单独验证线程安全性
        // 这个测试会暴露线程安全问题：多次运行可能得到不同的实例
        SingletonTestUtil.testSingletonThreadSafety(SingletonExample03.class.getSimpleName(), SingletonExample03::getInstance);
    }
}

