package com.coderlee.concurrent.design.promise.sync;

import lombok.Getter;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 文件同步Promise工厂类，负责创建并返回一个异步初始化完成后的FileSyncer实例。
 */
public class FileSyncerPromisor {

    // 单例模式保证全局唯一
    @Getter
    private static final FileSyncerPromisor INSTANCE = new FileSyncerPromisor();

    // 私有构造函数禁止外部实例化
    private FileSyncerPromisor() {}

    /**
     * 异步初始化FileSyncer，并将其包装成Future返回。
     *
     * @param config 配置信息对象
     * @return 返回包含FileSyncer实例的Future对象
     */
    public Future<FileSyncer> execute(FileSyncerConfig config) {
        FutureTask<FileSyncer> futureTask = new FutureTask<>(() -> {
            FileSyncer fileSyncer = new FileSyncerImpl(); // 创建FileSyncer实现类
            fileSyncer.connect(config.getServerAddress(), config.getUsername(), config.getPassword(), config.getServerDir()); // 初始化连接
            return fileSyncer; // 返回已连接好的FileSyncer
        });

        // 使用自定义线程池执行任务
        FileSyncerThreadPool.execute(futureTask);
        return futureTask;
    }
}

