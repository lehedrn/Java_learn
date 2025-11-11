package com.coderlee.concurrent.design.pc.right;

import com.coderlee.concurrent.design.thread.AbstractTerminationThread;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * 文件索引线程类，负责处理文件索引的生成
 *
 * @see AbstractTerminationThread
 */
@Slf4j
public class FileIndexThread extends AbstractTerminationThread {

    /**
     * 文件信息通道，用于获取待处理的文件信息
     */
    private final Channel<FileInfo> fileInfoChannel;

    /**
     * 构造函数
     *
     * @param fileInfoChannel 文件信息通道
     */
    public FileIndexThread(Channel<FileInfo> fileInfoChannel) {
        this.fileInfoChannel = fileInfoChannel;
    }

    /**
     * 执行文件索引生成任务
     *
     * @throws InterruptedException 当线程被中断时抛出
     */
    @Override
    protected void doRun() throws InterruptedException {
        // 从通道中获取文件信息
        FileInfo fileInfo = fileInfoChannel.take();
        log.info("异步获取到文件, 文件名: {}", fileInfo.getFileName());
        try {
            // 生成文件索引
            this.buildFileIndex(fileInfo);
        } catch(Exception e) {
            log.error("生成文件索引异常", e);
        } finally {
            // 减少未执行任务计数
            terminationToken.noExecuteTaskCount.decrementAndGet();
        }
    }

    /**
     * 生成文件索引
     *
     * @param fileInfo 文件信息
     * @throws InterruptedException 当线程被中断时抛出
     */
    private void buildFileIndex(FileInfo fileInfo) throws InterruptedException {
        log.info("生成文件索引开始, 文件名: {}", fileInfo.getFileName());
        Instant start = Instant.now();
        // 模拟索引生成耗时
        TimeUnit.SECONDS.sleep(2);
        log.info("生成文件索引结束, 文件名: {} , 耗时: {} ms", fileInfo.getFileName(), Duration.between(start, Instant.now()).toMillis());
    }
}
