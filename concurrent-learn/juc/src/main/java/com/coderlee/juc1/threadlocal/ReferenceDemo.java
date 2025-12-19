package com.coderlee.juc1.threadlocal;

import java.lang.ref.*;
import java.util.ArrayList;
import java.util.List;

import com.coderlee.juc1.utils.SleepUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Java 四种引用类型演示类
 *
 * 展示强引用、软引用、弱引用和虚引用在不同场景下的行为特征
 */
@Slf4j
public class ReferenceDemo {
    public static void main(String[] args) {
//        strongReference();    // 强引用演示
//        softReference();      // 软引用演示
//        weakReference();      // 弱引用演示
        phantomReference();     // 虚引用演示
    }

    /**
     * 虚引用(Phantom Reference)演示
     *
     * 特点：
     * 1. 无法通过 get() 方法获取对象实例（始终返回null）
     * 2. 必须与引用队列(ReferenceQueue)联合使用
     * 3. 对象被垃圾回收时会进入引用队列，可用于监听对象被回收的事件
     * 4. 主要用于跟踪对象被垃圾回收的过程
     */
    private static void phantomReference() {
        // 创建对象实例
        MyObject myObject = new MyObject();
        // 创建引用队列
        ReferenceQueue<MyObject> referenceQueue = new ReferenceQueue<>();
        // 创建虚引用，必须关联引用队列
        PhantomReference<MyObject> phantomReference = new PhantomReference<>(myObject, referenceQueue);

        // 虚引用的get()方法始终返回null
        log.info("虚引用，对象一直是null, reference---> {}", phantomReference.get());

        // 创建列表用于消耗内存
        List<byte[]> list = new ArrayList<>();

        // 线程1：不断分配内存，触发GC
        new Thread(() -> {
            while (true) {
                list.add(new byte[1024 * 1024]); // 每次分配1MB内存
                SleepUtils.sleep(500);
                log.info("list add success, current reference ---> {}", phantomReference.get());
            }
        }, "t1").start();

        // 线程2：监控引用队列，检测对象是否被回收
        new Thread(() -> {
            while (true) {
                // 检查引用队列中是否有元素
                Reference<? extends MyObject> reference = referenceQueue.poll();
                if (reference != null) {
                    log.info("虚引用对象被jvm回收了，reference---> {}", reference);
                    break; // 检测到对象被回收则退出循环
                }
            }
        }, "t2").start();
    }

    /**
     * 弱引用(Weak Reference)演示
     *
     * 特点：
     * 1. 生命周期比软引用更短
     * 2. 只要发生垃圾回收就会被回收（无论内存是否充足）
     * 3. 常用于实现缓存和避免内存泄漏
     */
    private static void weakReference() {
        // 创建弱引用对象
        WeakReference<MyObject> softReference = new WeakReference<>(new MyObject());
        log.info("内存够用，gc before: {}", softReference.get());

        // 显式触发垃圾回收
        System.gc();
        SleepUtils.sleep(3000); // 等待GC完成

        log.info("内存够用，gc after: {}", softReference.get());
    }

    /**
     * 软引用(Soft Reference)演示
     *
     * 特点：
     * 1. 内存充足时不回收
     * 2. 内存不足时会被回收
     * 3. 常用于实现内存敏感的高速缓存
     * 4. 可以配合ReferenceQueue使用来跟踪对象回收
     */
    private static void softReference() {
        // 创建软引用对象
        SoftReference<MyObject> softReference = new SoftReference<>(new MyObject());
        log.info("gc before: {}", softReference.get());

        // 显式触发垃圾回收
        System.gc();
        SleepUtils.sleep(3000); // 等待GC完成

        log.info("gc after: {}  -----> 内存够用", softReference.get());

        // 配置VM参数：-Xms10m -Xmx10m
        try {
            // 分配大块内存，触发OutOfMemoryError，迫使JVM回收软引用对象
            byte[] bytes = new byte[20 * 1024 * 1024]; // 20MB
        } catch (Exception e) {
            log.error("error", e);
        } finally {
            log.info("gc after: {}  -----> 内存不够用", softReference.get());
        }
    }

    /**
     * 强引用(Strong Reference)演示
     *
     * 特点：
     * 1. 最常见的引用类型
     * 2. 即使内存不足也不会回收，宁愿抛出OutOfMemoryError
     * 3. 只有当引用为null时才会被回收
     */
    private static void strongReference() {
        // 创建强引用
        MyObject o = new MyObject();
        log.info("gc before: {}", o);

        // 将引用置为null，断开引用链
        o = null;
        // 显式触发垃圾回收
        System.gc();

        SleepUtils.sleep(3000); // 等待GC完成
        log.info("gc after: {}", o);
    }
}

/**
 * 自定义对象类，重写了 finalize 方法用于观察对象回收过程
 */
@Slf4j
class MyObject {
    /**
     * 当对象被垃圾回收时会调用此方法（不推荐使用）
     * 仅用于演示目的，实际开发中应避免依赖此方法
     */
    @Override
    protected void finalize() throws Throwable {
        log.info("finalize method executed");
        super.finalize(); // 调用父类的finalize方法
    }
}
