package com.coderlee.juc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AtomicDemo 类用于演示原子性操作与非原子性操作的区别。
 * 通过创建多个线程分别调用 GeneralInteger与 AtomicInt 类，
 * 展示在多线程环境下普通整型变量和原子类的线程安全性差异。
 */
public class AtomicDemo {

    public static void main(String[] args) {
        // handler1();
        handler2();
        // printResult();
    }

    /**
     * 使用 AtomicInt 类进行多线程测试。
     * 创建 100,000 个线程，每个线程执行原子性递增操作。
     * 目的是验证 `AtomicInteger` 在高并发下的线程安全性。
     * 
     * 先在Main方法中运行该程序，并查看输出结果，使用命令:
     * java -cp /home/workspace/coderlee/java_projects/java_learn/juc/target/classes com.coderlee.juc.AtomicDemo > output.txt
     * 再运行 `printResult()` 方法，查看输出结果。
     */
    public static void handler2() {
        AtomicInt atomicInt = new AtomicInt();
        for (int i = 0; i < 100000; i++) {
            new Thread(atomicInt).start();
        }
    }

    /**
     * 使用 GeneralInteger 类进行多线程测试。
     * 创建 100,000 个线程，每个线程执行非原子性递增操作。
     * 目的是展示普通整型变量在高并发下的线程不安全性。
     * 使用命令: java -cp /home/workspace/coderlee/java_projects/java_learn/juc/target/classes com.coderlee.juc.AtomicDemo > output.txt
     * 再运行 `printResult()` 方法，查看输出结果。
     */
    public static void handler1() {
        GeneralInteger generalInteger = new GeneralInteger();
        for (int i = 0; i < 100000; i++) {
            new Thread(generalInteger).start();
        }
    }

    /**
     * 打印并分析输出结果。
     * 读取 `output.txt` 文件中的内容，检查是否存在缺失的数字。
     * 如果存在缺失，则说明对应的操作在多线程环境下未保证线程安全性。
     */
    public static void printResult() {
        HashSet<Integer> set = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("/home/workspace/coderlee/java_projects/java_learn/output.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                set.add(Integer.valueOf(line.trim()));
            }
            System.out.println("set size: " + set.size());
            for (int i = 0; i < 100000; i++) {
                if (!set.contains(i)) {
                    System.out.println("set miss: " + i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * GeneralInteger 类实现了 Runnable 接口，用于模拟非原子性操作。
 * 该类包含一个非线程安全的整型变量 serialNumber，并提供递增方法。
 */
class GeneralInteger implements Runnable {

    private int serialNumber = 0;

    /**
     * 线程执行的方法。
     * 每个线程在休眠 200ms 后调用 getSerialNumber() 方法获取当前值并打印。
     */
    @Override
    public void run() {
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.getSerialNumber());
    }

    /**
     * 获取当前序列号并递增。
     * 由于该操作非原子性，在多线程环境下可能导致数据竞争问题。
     *
     * @return 当前序列号的值。
     */
    public int getSerialNumber() {
        return serialNumber++;
    }

    /**
     * 设置序列号的值。
     *
     * @param serialNumber 要设置的序列号值。
     */
    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }
}

/**
 * AtomicInt 类实现了 Runnable 接口，用于模拟原子性操作。
 * 该类使用 `AtomicInteger` 提供线程安全的递增操作。
 */
class AtomicInt implements Runnable {

    private AtomicInteger serialNumber = new AtomicInteger(0);

    /**
     * 线程执行的方法。
     * 每个线程在休眠 200ms 后调用 getSerialNumber() 方法获取当前值并打印。
     */
    @Override
    public void run() {
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.getSerialNumber());
    }

    /**
     * 获取当前序列号并原子性递增。
     * 由于使用了 `AtomicInteger`，该操作在多线程环境下是线程安全的。
     *
     * @return 当前序列号的值。
     */
    public int getSerialNumber() {
        return this.serialNumber.getAndIncrement();
    }
}