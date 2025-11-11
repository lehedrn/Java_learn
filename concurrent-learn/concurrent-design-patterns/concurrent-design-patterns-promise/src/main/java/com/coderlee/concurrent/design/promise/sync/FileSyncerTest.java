package com.coderlee.concurrent.design.promise.sync;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件同步测试启动类，用于演示如何使用该同步框架。
 */
@Slf4j
public class FileSyncerTest {

    public static void main(String[] args) {

        // 添加JVM关闭钩子以便优雅地关闭线程池
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("触发钩子函数, 关闭线程池");
            FileSyncerClientThreadPool.shutdown();
            FileSyncerThreadPool.shutdown();
        }));

        // 创建配置对象
        FileSyncerConfig config = new FileSyncerConfig("127.0.0.1", "coderlee", "lihaidong", "/home/workspace/coderlee");

        // 创建同步任务
        FileSyncerTask task = new FileSyncerTask(config);

        // 提交任务至客户端线程池执行
        FileSyncerClientThreadPool.execute(task);
    }
}
