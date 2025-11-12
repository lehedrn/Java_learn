/**
 * 消息服务默认实现类
 * <p>
 * 提供消息发送功能的具体实现，模拟了实际的消息推送操作，
 * 包含日志记录和耗时统计功能。
 * </p>
 */
package com.coderlee.concurrent.design.thread.pool.common;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MessageServiceImpl implements MessageService {
    /**
     * 发送指定内容的消息
     * <p>
     * 模拟实际的消息推送操作，包含2秒的延迟以模拟网络请求，
     * 并记录操作开始时间、结束时间和总耗时。
     * </p>
     *
     * @param message 要发送的消息内容，不能为空
     */
    @Override
    public void sendMessage(String message) {
        // 记录消息推送开始日志
        log.info("{}-消息推送开始", Thread.currentThread().getName());

        // 记录操作开始时间
        Instant start = Instant.now();

        try {
            // 模拟网络请求，休眠2秒
            TimeUnit.SECONDS.sleep(2);

            // 记录消息推送完成日志
            log.info("{}-消息推送完毕, 推送的消息为: {}", Thread.currentThread().getName(), message);
        } catch (InterruptedException e) {
            // 处理线程中断异常
            throw new RuntimeException(e);
        }

        // 记录消息推送结束日志和耗时统计
        log.info("{}-消息推送结束, 耗时: {} ms", Thread.currentThread().getName(), Duration.between(start, Instant.now()).toMillis());
    }
}
