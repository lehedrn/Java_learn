/**
 * 报警系统测试类
 *
 * 该类演示了两种不同的两阶段终止模式实现方式：
 * - {@link #main_1()} 方法展示了基本的两阶段终止模式，通过外部中断信号来停止线程
 * - {@link #main_2()} 方法展示了改进的两阶段终止模式，线程会在队列为空时自动终止
 *
 * @see AlarmQueue
 * @see AlarmInfo
 * @see AlarmType
 */
package com.coderlee.concurrent.design.two.phase.alarm.wrong;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class AlarmTest {

    /**
     * 程序入口点
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 执行main_2方法，展示自动终止的两阶段终止模式
//        main_1();
        main_2();
    }

    /**
     * 改进的两阶段终止模式实现
     *
     * 该方法创建一个报警任务执行线程，当队列中没有更多任务时，
     * 线程会自动检查并终止自己，而不需要外部中断信号。
     */
    public static void main_2() {
        try {
            // 向报警队列中添加100个报警信息
            for (int i = 1; i <= 100; i++) {
                AlarmQueue.getInstance().put(new AlarmInfo(String.valueOf(i), AlarmType.FAULT, String.valueOf(i).concat("出故障了")));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 记录初始任务数量
        log.info("初始任务时未完成的任务数量: {}", AlarmQueue.getInstance().size());

        // 创建并启动报警任务执行线程
        Thread executeTaskThread = new Thread(() -> {
            Thread currentThread = Thread.currentThread();
            while (true) {
                // 检查线程是否被中断，如果是则记录剩余任务数量并退出循环
                if (currentThread.isInterrupted()) {
                    log.info("线程被中断时未完成的任务数量是: {}", AlarmQueue.getInstance().size());
                    break;
                }

                // 检查队列是否为空，如果为空则主动中断当前线程
                if (AlarmQueue.getInstance().size() <= 0) {
                    currentThread.interrupt();
                }

                try {
                    // 从队列中取出并处理报警信息
                    AlarmInfo alarmInfo = AlarmQueue.getInstance().take();
                    log.info("上报告警信息: {}", alarmInfo);

                    // 模拟处理报警信息的时间消耗
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    // 处理中断异常，重新设置中断状态
                    e.printStackTrace();
                    currentThread.interrupt();
                }
            }
        });

        // 启动报警任务执行线程
        executeTaskThread.start();

        try {
            // 主线程等待2秒后继续执行
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 基本的两阶段终止模式实现
     *
     * 该方法创建一个报警任务执行线程，并在2秒后通过外部中断信号
     * 来终止线程执行。
     */
    public static void main_1() {
        try {
            // 向报警队列中添加100个报警信息
            for (int i = 1; i <= 100; i++) {
                AlarmQueue.getInstance().put(new AlarmInfo(String.valueOf(i), AlarmType.FAULT, String.valueOf(i).concat("出故障了")));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 记录初始任务数量
        log.info("初始任务时未完成的任务数量: {}", AlarmQueue.getInstance().size());

        // 创建并启动报警任务执行线程
        Thread executeTaskThread = new Thread(() -> {
            Thread currentThread = Thread.currentThread();
            while (true) {
                // 检查线程是否被中断，如果是则记录剩余任务数量并退出循环
                if (currentThread.isInterrupted()) {
                    log.info("线程被中断时未完成的任务数量是: {}", AlarmQueue.getInstance().size());
                    break;
                }

                try {
                    // 从队列中取出并处理报警信息
                    AlarmInfo alarmInfo = AlarmQueue.getInstance().take();
                    log.info("上报告警信息: {}", alarmInfo);

                    // 模拟处理报警信息的时间消耗
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    // 处理中断异常，重新设置中断状态
                    e.printStackTrace();
                    currentThread.interrupt();
                }
            }
        });

        // 启动报警任务执行线程
        executeTaskThread.start();

        try {
            // 主线程等待2秒后继续执行
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 主动中断报警任务执行线程
        executeTaskThread.interrupt();
    }
}
