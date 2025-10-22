package com.coderlee.juc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWriteArrayListDemo 类用于演示 `CopyOnWriteArrayList` 和 `Collections.synchronizedList`
 * 在多线程环境下的行为差异。
 * 
 * 主要对比两种线程安全集合的实现方式及其在并发迭代与修改操作中的表现：
 * - `Collections.synchronizedList` 提供基本的线程安全，但无法避免 `ConcurrentModificationException`。
 * - `CopyOnWriteArrayList` 是专为并发设计的集合类，写操作会复制底层数组，从而避免迭代异常。
 * 
 * 注意:
 *   - `CopyOnWriteArrayList` 适用于读多写少的场景，因为写操作会复制底层数组，从而避免迭代异常。
 */
public class CopyOnWriteArrayListDemo {

    public static void main(String[] args) {
        // handler1(); // 调用此方法测试 `Collections.synchronizedList` 的行为
        handler2(); // 调用此方法测试 `CopyOnWriteArrayList` 的行为
    }

    /**
     * handler2 方法用于测试 `CopyOnWriteArrayList` 在多线程环境下的表现。
     * 创建 10 个线程并发操作 CopyOnWriteArrayListThread ，观察其线程安全性及迭代行为。
     */
    public static void handler2() {
        CopyOnWriteArrayListThread cowt = new CopyOnWriteArrayListThread();
        for (int i = 0; i < 10; i++) {
            new Thread(cowt).start();
        }
    }

    /**
     * handler1 方法用于测试 `Collections.synchronizedList` 在多线程环境下的表现。
     * 创建 10 个线程并发操作 ArrayListThread，观察其线程安全性及迭代行为。
     */
    public static void handler1() {
        ArrayListThread alt = new ArrayListThread();
        for (int i = 0; i < 10; i++) {
            new Thread(alt).start();
        }
    }
}

/**
 * ArrayListThread 实现了 Runnable 接口，用于测试 `Collections.synchronizedList` 的线程安全性。
 * - 使用 `Collections.synchronizedList` 包装一个 `ArrayList`，提供基本的线程安全。
 * - 在多线程环境下，尝试在迭代过程中修改列表内容，可能会抛出 `ConcurrentModificationException`。
 */
class ArrayListThread implements Runnable {

    // 使用 `Collections.synchronizedList` 包装的线程安全列表
    private static List<String> list = Collections.synchronizedList(new ArrayList<>());

    static {
        // 初始化列表，添加 3 个元素
        for (int i = 0; i < 3; i++) {
            list.add("Thread" + i);
        }
    }

    @Override
    public void run() {
        // 获取列表的迭代器
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            System.out.println(it.next()); // 打印当前元素
            list.add("AAA"); // 尝试在迭代过程中修改列表
        }
    }
}

/**
 * CopyOnWriteArrayListThread 实现了 Runnable 接口，用于测试 `CopyOnWriteArrayList` 的线程安全性。
 * - 使用 `CopyOnWriteArrayList`，专为并发设计，写操作会复制底层数组。
 * - 在多线程环境下，迭代过程中不会抛出 `ConcurrentModificationException`。
 */
class CopyOnWriteArrayListThread implements Runnable {

    // 使用 `CopyOnWriteArrayList` 提供的线程安全列表
    private static List<String> list = new CopyOnWriteArrayList<>();

    static {
        // 初始化列表，添加 3 个元素
        for (int i = 0; i < 3; i++) {
            list.add("Thread" + i);
        }
    }

    @Override
    public void run() {
        // 获取列表的迭代器
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            System.out.println(it.next()); // 打印当前元素
            list.add("AAA"); // 在迭代过程中修改列表，不会抛出异常
        }
    }
}