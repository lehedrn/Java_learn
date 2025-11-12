/**
 * 正确使用线程池的消息发送示例
 * <p>
 * 展示了如何正确配置和使用线程池来处理并发消息发送任务，
 * 避免创建过多线程导致系统资源耗尽的问题。
 * </p>
 */
package com.coderlee.concurrent.design.thread.pool.right;

import com.coderlee.concurrent.design.thread.pool.common.MessageService;
import com.coderlee.concurrent.design.thread.pool.common.MessageServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class MessageRightThreadPool {
    /**
     * 自定义配置的线程池实例
     * <p>
     * 核心线程数为CPU核心数+1，最大线程数64，队列容量1024，
     * 空闲线程存活时间60秒，使用CallerRunsPolicy拒绝策略。
     * </p>
     */
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(
            // 核心线程数设置为CPU核心数+1
            Runtime.getRuntime().availableProcessors() + 1,
            // 最大线程数设置为64
            64,
            // 空闲线程存活时间60秒
            60,
            TimeUnit.SECONDS,
            // 使用容量为1024的有界队列
            new LinkedBlockingQueue<>(1024),
            // 自定义线程命名工厂
            r -> new Thread(r, "message-send-thread"),
            // 使用调用者线程执行的拒绝策略
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    /**
     * 要发送的消息总数
     */
    private static final long MESSAGE_COUNT = 1000;

    /**
     * 程序入口点
     * <p>
     * 创建消息服务实例，使用线程池执行1000个消息发送任务，
     * 并注册JVM关闭钩子以优雅关闭线程池。
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
