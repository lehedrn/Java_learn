package com.coderlee.concurrent.design.thread.close.common;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * 文件客户端类，用于模拟文件下载操作
 * <p>该类提供了初始化客户端和下载文件的功能，模拟了耗时的IO操作</p>
 */
@Slf4j
public class FileClient {

    /**
     * 初始化文件客户端
     * <p>模拟客户端初始化过程，耗时约1秒</p>
     */
    public void initClient() {
        // 记录初始化开始日志
        log.info("{} 初始化文件客户端开始", Thread.currentThread().getName());
        // 记录开始时间
        Instant start = Instant.now();
        try {
            // 模拟初始化耗时
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 记录初始化完成日志及耗时
        log.info("{} 初始化文件客户端完毕, 耗时: {} ms", Thread.currentThread().getName(), Duration.between(start, Instant.now()).toMillis());
    }

    /**
     * 下载指定文件
     *
     * @param fileName 要下载的文件名
     */
    public void downloadFile(String fileName) {
        // 记录下载开始日志
        log.info("{} 下载文件开始", Thread.currentThread().getName());
        // 记录开始时间
        Instant start = Instant.now();
        try {
            // 模拟下载耗时
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 记录下载完成日志及耗时
        log.info("{} 下载文件完毕, 下载的文件为: {}, 耗时: {} ms", Thread.currentThread().getName(), fileName, Duration.between(start, Instant.now()).toMillis());
    }
}
