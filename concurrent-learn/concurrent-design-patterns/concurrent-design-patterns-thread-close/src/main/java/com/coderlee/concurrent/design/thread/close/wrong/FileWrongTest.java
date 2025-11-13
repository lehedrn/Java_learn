package com.coderlee.concurrent.design.thread.close.wrong;

import java.util.concurrent.CountDownLatch;

/**
 * 错误的线程使用示例
 * <p>直接创建多个线程执行任务，没有统一的线程管理，可能导致资源浪费</p>
 *
 * @see com.coderlee.concurrent.design.thread.close.right.FileRightTest 正确的实现方式
 */
public class FileWrongTest {

    // 并发下载任务数量
    private static final int COUNT = 10;

    /**
     * 主函数，执行文件下载测试
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 创建倒计时门闩，用于等待所有任务完成
        CountDownLatch countDownLatch = new CountDownLatch(COUNT);
        // 创建文件服务实例
        FileService fileService = new FileServiceImpl();

        // 启动多个线程并发下载文件
        for (int i = 1; i <= COUNT; i++) {
            // 使用final变量捕获循环变量
            final int index = i;
            // 创建并启动下载线程
            new Thread(() -> {
                fileService.downloadFile("coderlee00" + index);
                // 任务完成后减少门闩计数
                countDownLatch.countDown();
            }, "download-file-" + i + "-").start();
        }

        try {
            // 等待所有下载任务完成
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 注意：这里没有显式的线程关闭操作
    }
}
