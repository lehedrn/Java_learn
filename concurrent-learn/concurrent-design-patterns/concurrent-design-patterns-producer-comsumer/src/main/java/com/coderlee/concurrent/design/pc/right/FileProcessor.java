package com.coderlee.concurrent.design.pc.right;

import com.coderlee.concurrent.design.thread.AbstractTerminationThread;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 文件处理器类，负责文件上传和索引处理的协调
 */
@Slf4j
public class FileProcessor {

    /**
     * 文件信息通道，用于在文件上传和索引处理之间传递数据
     */
    private final Channel<FileInfo> fileInfoChannel = new BlockingQueueChannel<>(new ArrayBlockingQueue<>(1024));

    /**
     * 文件索引处理线程
     */
    private final AbstractTerminationThread indexThread = new FileIndexThread(fileInfoChannel);

    /**
     * 上传文件
     *
     * @param fileInfo 文件信息
     */
    public void uploadFile(FileInfo fileInfo) {
        log.info("上传文件开始, 文件名: {}", fileInfo.getFileName());
        Instant start = Instant.now();
        try {
            // 将文件信息放入通道
            fileInfoChannel.put(fileInfo);
            // 增加未执行任务计数
            indexThread.terminationToken.noExecuteTaskCount.incrementAndGet();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("上传文件结束, 文件名: {}, 耗时: {} ms", fileInfo.getFileName(), Duration.between(start, Instant.now()).toMillis());
    }

    /**
     * 启动索引处理线程
     */
    public void startIndexThread() {
        indexThread.start();
    }

    /**
     * 关闭索引处理线程
     */
    public void shutdownIndexThread() {
        indexThread.terminate();
    }
}
