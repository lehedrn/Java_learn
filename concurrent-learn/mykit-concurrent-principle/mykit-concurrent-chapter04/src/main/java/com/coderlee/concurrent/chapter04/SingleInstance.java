package com.coderlee.concurrent.chapter04;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * 不安全的单例对象实现
 * 
 * <p>该类演示了一个线程不安全的单例模式实现。虽然使用了双重检查锁定(Double-Check Locking)模式，
 * 但由于缺少volatile关键字，仍然存在线程安全问题。多个线程可能创建出不同的实例。</p>
 * 
 * <p>存在的问题：
 * <ul>
 *   <li>没有使用volatile关键字修饰instance变量，可能导致指令重排序问题</li>
 *   <li>在多线程环境下可能创建多个实例</li>
 * </ul>
 * </p>
 */
public class SingleInstance {

    /**
     * 单例实例变量，缺少volatile修饰符导致线程不安全
     */
    private static SingleInstance instance;

    /**
     * 私有构造函数，防止外部直接实例化
     */
    private SingleInstance() {
    }

    /**
     * 获取单例实例的方法
     * 
     * <p>采用双重检查锁定(Double-Check Locking)模式实现懒加载单例，
     * 但由于缺少volatile关键字，仍存在线程安全隐患：</p>
     * 
     * <ol>
     *   <li>第一次检查：避免不必要的同步，提高性能</li>
     *   <li>同步块：确保同一时间只有一个线程能进入创建实例的代码段</li>
     *   <li>第二次检查：避免重复创建实例</li>
     * </ol>
     * 
     * @return 单例实例
     */
    public static SingleInstance getInstance() {
        // 第一次检查：如果实例已经创建，则直接返回，避免不必要的同步
        if (instance == null) {
            try {
                // 模拟创建实例前的一些准备工作
                TimeUnit.MILLISECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 创建对象时加锁，确保线程安全
            synchronized (SingleInstance.class) {
                try {
                    // 模拟同步块内的一些处理工作
                    TimeUnit.MILLISECONDS.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 第二次检查：确保只创建一个实例
                if (instance == null) {
                    try {
                        // 模拟实例创建过程中的工作
                        TimeUnit.MILLISECONDS.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 创建新的单例实例
                    instance = new SingleInstance();
                }
            }
        }
        return instance;
    }
}

/**
 * 单例测试类
 * 
 * <p>用于测试SingleInstance类的线程安全性。通过启动100个线程并发调用getInstance()方法，
 * 并打印每个实例的hashCode来验证是否真的只创建了一个实例。</p>
 * 
 * <p>在当前的实现中，由于缺少volatile关键字，可能会出现多个不同的hashCode值，
 * 证明存在线程安全问题。</p>
 */
@Slf4j
class SingleInstanceTest { 
    /**
     * 主方法，启动多个线程测试单例实现的线程安全性
     * 
     * <p>创建100个线程，并发调用SingleInstance.getInstance()方法获取实例，
     * 通过打印实例的hashCode来判断是否创建了多个实例。</p>
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 循环创建100个线程进行并发测试
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                // 打印获取到的单例实例的hashCode，用于判断是否为同一实例
                log.info("{}", SingleInstance.getInstance().hashCode());
            }).start();
        }
    }
}