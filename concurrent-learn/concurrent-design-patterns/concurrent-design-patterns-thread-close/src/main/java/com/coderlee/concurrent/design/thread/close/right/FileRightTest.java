package com.coderlee.concurrent.design.thread.close.right;

import java.util.concurrent.CountDownLatch;

/**
 * 正确的线程关闭测试类
 * <p>演示如何正确地管理和关闭线程资源，确保所有任务执行完毕后才关闭线程</p>
 *
 * @see FileService
 * @see FileServiceImpl
 */
public class FileRightTest {

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
        // 初始化文件服务
        fileService.init();

        // 启动多个线程并发下载文件
        for (int i = 1; i <= COUNT; i++) {
            // 使用final变量捕获循环变量
            final int index = i;
            // 创建并启动下载线程
            new Thread(() -> {
                fileService.downloadFile("coderlee-" + index);
                // 任务完成后减少门闩计数
                countDownLatch.countDown();
            }, "download-file-" + index +"-").start();
        }

        try {
            // 等待所有下载任务完成
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 关闭文件服务
        fileService.shutdown();
    }
}
