package com.coderlee.concurrent.design.thread.pool.wrong;

import com.coderlee.concurrent.design.thread.pool.common.MessageService;
import com.coderlee.concurrent.design.thread.pool.common.MessageServiceImpl;

import java.util.concurrent.CountDownLatch;

/**
 * 错误的消息发送示例（直接创建线程方式）
 * <p>
 * 展示了不正确地为每个任务创建新线程的做法，
 * 这种方式在任务数量较多时会导致系统资源耗尽。
 * </p>
 */
public class MessageTest {
    /**
     * 程序入口点
     * <p>
     * 为每个消息发送任务创建一个新线程，这种方式在任务数量较大时会消耗大量系统资源。
     * 使用CountDownLatch等待所有任务完成。
     * </p>
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 设置要发送的消息数量
        int count = 500;

        // 创建CountDownLatch用于等待所有线程执行完成
        CountDownLatch countDownLatch = new CountDownLatch(count);

        // 创建消息服务实例
        MessageService messageService = new MessageServiceImpl();

        // 为每个消息创建一个新线程
        for (int i = 0; i < count; i++) {
            // 创建并启动新线程执行消息发送任务
            new Thread(() -> {
                // 发送消息
                messageService.sendMessage("恭喜您获得一张50元的优惠券");
                // 计数器减1
                countDownLatch.countDown();
            }, "message-send-thread-" + i).start();
        }

        try {
            // 等待所有线程执行完成
            countDownLatch.await();
        } catch (InterruptedException e) {
            // 处理线程中断异常
            throw new RuntimeException(e);
        }
    }
}
