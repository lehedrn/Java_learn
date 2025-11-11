package com.coderlee.concurrent.design.promise.sync;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 本地文件扫描工具类，用于查找并收集待同步的文件信息。
 */
@Slf4j
public class FileSyncerScanner {

    // 单例模式保证全局唯一
    private static final FileSyncerScanner FILE_SYNCER_SCANNER = new FileSyncerScanner();

    // 私有构造函数防止实例化
    private FileSyncerScanner() {
    }

    /**
     * 获取单例实例。
     *
     * @return FileSyncerScanner 单例实例
     */
    public static FileSyncerScanner getInstance() {
        return FILE_SYNCER_SCANNER;
    }

    /**
     * 扫描指定目录下的所有文件并返回其基本信息列表。
     *
     * @param localDir 本地目录路径
     * @return 包含文件信息的列表
     */
    public List<FileSyncerInfo> scanFile(String localDir) {
        log.info("扫描本地文件开始");
        Instant start = Instant.now();
        try {
            TimeUnit.SECONDS.sleep(2); // 模拟IO读取延迟
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 构造虚拟文件信息
        byte[] file = localDir.getBytes(StandardCharsets.UTF_8);
        List<FileSyncerInfo> fileSyncerInfoList = Arrays.asList(new FileSyncerInfo(file, localDir, file.length));

        log.info("扫描本地文件结束，耗时：{} ms", Duration.between(start, Instant.now()).toMillis());
        return fileSyncerInfoList;
    }
}

