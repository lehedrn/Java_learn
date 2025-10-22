package com.coderlee.juc;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * CountDownLanchDemo 类用于演示如何使用 CountDownLatch 实现线程间的同步。
 * 
 * 功能描述：
 * 1. 主线程启动 10 个子线程，并等待所有子线程完成任务后继续执行。
 * 2. 使用 CountDownLatch 协调主线程与子线程的执行顺序。
 * 3. 子线程的任务是统计一个随机区间内的偶数个数，并记录耗时。
 * 4. 主线程记录从开始到所有子线程完成的总耗时，用于性能测试。
 */
public class CountDownLanchDemo {

    public static void main(String[] args) {
        // 初始化 CountDownLatch，计数器值为 10，表示需要等待 10 个线程完成
        final CountDownLatch lanch = new CountDownLatch(10);
        
        // 创建 LanchDemo 实例，将 CountDownLatch 传递给每个线程任务
        LanchDemo ld = new LanchDemo(lanch);
        
        System.out.println("开始执行");
        long start = System.currentTimeMillis(); // 记录主线程开始时间
        
        // 启动 10 个线程，每个线程执行 LanchDemo 的 run 方法
        for (int i = 0; i < 10; i++) {
            new Thread(ld).start();
        }
        
        try {
            // 主线程阻塞，等待所有子线程完成（计数器减到 0）
            lanch.await();
        } catch (InterruptedException e) {
            e.printStackTrace(); // 捕获并处理中断异常
        }
        
        // 所有子线程完成后，打印总耗时
        System.out.println("执行结束，耗时：" + (System.currentTimeMillis() - start));
    }

}

/**
 * LanchDemo 类实现了 Runnable 接口，用于定义子线程的任务逻辑。
 * 
 * 功能描述：
 * 1. 每个子线程执行一个随机长度的循环，统计区间 [0, length) 内偶数的个数。
 * 2. 在任务完成后，减少 CountDownLatch 的计数器值，通知主线程任务已完成。
 * 3. 使用 try-finally 确保即使发生异常，计数器也能正确减少。
 */
class LanchDemo implements Runnable {

    private CountDownLatch lanch;

    // 构造函数，接收 CountDownLatch 实例
    public LanchDemo(CountDownLatch lanch) {
        this.lanch = lanch;
    }

    @Override
    public void run() {
        try {
            long start = System.currentTimeMillis(); // 记录当前线程开始时间
            
            int count = 0; // 统计偶数个数
            int length = new Random().nextInt(99999999); // 随机生成一个大整数作为循环长度
            
            // 遍历 [0, length)，统计偶数个数
            for (int i = 0; i < length; i++) {
                if (i % 2 == 0) {
                    count++;
                }
            }
            
            // 打印当前线程的名称、区间范围、偶数个数以及耗时
            System.out.println(Thread.currentThread().getName() + "[0 ~ " + length + "]有偶数：" + count + " 个，耗时：" + (System.currentTimeMillis() - start));
        } finally {
            // 确保无论是否发生异常，都会减少计数器的值
            lanch.countDown();
        }
    }
    
}