package com.coderlee.juc1.volatiles;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * volatile关键字不能保证原子性操作的演示类
 * 通过对比有synchronized修饰的方法和仅有volatile修饰的变量在多线程环境下的表现
 * 展示了volatile只能保证可见性，不能保证复合操作的原子性
 */
@Slf4j
public class VolatileNoAtomicDemo {

    public static void main(String[] args) {
        // 演示使用synchronized关键字的情况
        demo1();
        // 演示仅使用volatile关键字的情况
        demo2();
        // 改进版演示使用synchronized关键字的情况
        demo1Adv();
        // 改进版演示仅使用volatile关键字的情况
        demo2Adv();
    }

    /**
     * 改进版演示使用synchronized关键字的情况
     * 使用封装的run方法执行测试
     */
    public static void demo1Adv() {
        MyNumber myNumber = new MyNumber();
        run(x -> myNumber.addPlusPlus(), () -> myNumber.number, "synchronized");
    }

    /**
     * 改进版演示仅使用volatile关键字的情况
     * 使用封装的run方法执行测试
     */
    public static void demo2Adv() {
        MyNumber2 myNumber = new MyNumber2();
        run(x -> myNumber.addPlusPlus(), () -> myNumber.number, "volatile");
    }

    /**
     * 通用的多线程执行方法
     * 创建多个线程并执行指定操作，最后输出结果
     *
     * @param operation 要执行的操作
     * @param result 结果获取函数
     * @param msg 输出消息前缀
     */
    public static void run(Consumer<Void> operation, Supplier<Integer> result, String msg) {
        // 创建10个线程
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                // 每个线程执行1000次操作
                for (int j = 1; j <= 1000; j++) {
                    operation.accept(null);
                }
            }, String.valueOf(i)).start();
        }
        // 等待所有线程执行完毕
        SleepUtils.sleep(1000);
        // 输出最终结果
        log.info("{} result: {}", msg, result.get());
    }

    /**
     * 演示仅使用volatile关键字的情况
     * 预期结果会小于10000，因为volatile不保证++操作的原子性
     */
    public static void demo2() {
        // 创建MyNumber2实例
        MyNumber2 myNumber = new MyNumber2();
        // 启动10个线程
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                // 每个线程执行1000次自增操作
                for (int j = 1; j <= 1000; j++) {
                    myNumber.addPlusPlus();
                }
            }, String.valueOf(i)).start();
        }
        // 等待所有线程执行完毕
        SleepUtils.sleep(1000);
        // 输出最终结果，预期会小于10000
        log.info("number: {}", myNumber.number);
    }

    /**
     * 演示使用synchronized关键字的情况
     * 预期结果为10000，因为synchronized保证了++操作的原子性
     */
    public static void demo1() {
        // 创建MyNumber实例
        MyNumber myNumber = new MyNumber();
        // 启动10个线程
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                // 每个线程执行1000次自增操作
                for (int j = 1; j <= 1000; j++) {
                    myNumber.addPlusPlus();
                }
            }, String.valueOf(i)).start();
        }
        // 等待所有线程执行完毕
        SleepUtils.sleep(1000);
        // 输出最终结果，预期为10000
        log.info("number: {}", myNumber.number);
    }
}

/**
 * 使用synchronized关键字保证原子性的数字类
 * 通过synchronized关键字确保同一时刻只有一个线程能够修改number变量
 */
class MyNumber {
    // 普通int变量，不需要volatile因为通过synchronized保证可见性
    int number;

    /**
     * 使用synchronized关键字保证++操作的原子性
     * 同一时间只有一个线程能执行此方法
     * 确保了操作的原子性和可见性
     */
    public synchronized void addPlusPlus() {
        number++;
    }
}

/**
 * 使用volatile关键字但不保证原子性的数字类
 * 展示了volatile只能保证可见性而不能保证复合操作的原子性
 */
class MyNumber2 {
    // 使用volatile修饰的int变量，保证可见性但不保证原子性
    // 所有线程都能看到最新的值，但不能保证复合操作的原子性
    volatile int number;

    /**
     * 没有同步机制的自增方法
     * 虽然volatile保证了变量的可见性，但number++操作本身不是原子操作
     * 包含三个步骤：读取值、+1、写回值，在多线程环境下会出现竞态条件
     *
     * 具体执行过程：
     * 1. 从主内存读取number的当前值到工作内存
     * 2. 在工作内存中执行+1操作
     * 3. 将新值写回主内存
     *
     * 多线程环境下可能出现的问题：
     * 线程A读取number=0，线程B也读取number=0
     * 线程A执行+1得到1，写回主内存
     * 线程B执行+1得到1，写回主内存
     * 最终结果是1而不是期望的2
     */
    public void addPlusPlus() {
        number++;
    }
}
