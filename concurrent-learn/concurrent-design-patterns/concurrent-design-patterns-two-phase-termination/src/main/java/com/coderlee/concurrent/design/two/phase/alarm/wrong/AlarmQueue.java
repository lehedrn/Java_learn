package com.coderlee.concurrent.design.two.phase.alarm.wrong;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 报警信息队列类
 * <p>
 * 提供线程安全的报警信息队列，用于在报警生产者和消费者之间传递报警信息。
 * 使用单例模式确保全局只有一个队列实例。
 * </p>
 *
 * @see AlarmInfo 报警信息实体类
 */
public class AlarmQueue {
    /**
     * 阻塞队列实例，用于存储报警信息
     * <p>
     * 使用ArrayBlockingQueue实现，容量为1024
     * </p>
     */
    private static final BlockingQueue<AlarmInfo> INSTANCE = new ArrayBlockingQueue<>(1024);

    /**
     * 获取报警队列实例
     * <p>
     * 提供全局访问点，返回单例的报警队列实例
     * </p>
     *
     * @return 报警信息阻塞队列实例
     */
    public static BlockingQueue<AlarmInfo> getInstance() {
        return INSTANCE;
    }
}
