package com.coderlee.concurrent.design.thread.pool.wrong;

import com.coderlee.concurrent.design.thread.pool.common.MessageService;
import com.coderlee.concurrent.design.thread.pool.common.MessageServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 错误使用线程池的消息发送示例
 * <p>
 * 使用Executors.newCachedThreadPool()创建无界线程池，
 * 在高并发场景下可能导致创建过多线程，耗尽系统资源。
 * </p>
 */
@Slf4j
public class MessageWrongThreadPoolTest {
    /**
     * 使用Executors创建的无界线程池
     * <p>
     * newCachedThreadPool创建的线程池没有最大线程数限制，
     * 在高并发场景下可能导致创建过多线程，耗尽系统资源。
     * </p>
     */
    private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    /**
     * 要发送的消息总数
     */
    private static final long MESSAGE_COUNT = 1000;

    /**
     * 程序入口点
     * <p>
     * 使用无界线程池执行消息发送任务，在高并发场景下可能导致系统资源耗尽。
     * </p>
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 注册JVM关闭钩子，用于优雅关闭线程池
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 记录线程池关闭日志
            log.info("{} 执行关闭线程池的操作", Thread.currentThread().getName());
            // 关闭线程池
            THREAD_POOL.shutdown();
        }, "shutdown-hook-thread"));

        // 创建消息服务实例
        MessageService messageService = new MessageServiceImpl();

        // 提交1000个消息发送任务到线程池
        for (long i = 0; i < MESSAGE_COUNT; i++) {
            THREAD_POOL.execute(() -> messageService.sendMessage("恭喜您获取一张50元的优惠券"));
        }
    }
}
