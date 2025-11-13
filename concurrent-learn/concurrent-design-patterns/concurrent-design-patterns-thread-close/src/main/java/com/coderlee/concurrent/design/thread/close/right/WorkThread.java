package com.coderlee.concurrent.design.thread.close.right;

import com.coderlee.concurrent.design.thread.AbstractTerminationThread;
import com.coderlee.concurrent.design.thread.close.common.FileClient;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 工作线程类，负责处理文件下载任务
 * <p>继承自抽象终止线程类，实现了可优雅关闭的工作线程模式</p>
 *
 * @see AbstractTerminationThread
 * @see FileClient
 */
public class WorkThread extends AbstractTerminationThread {

    // 工作队列，用于存放待处理的文件下载任务
    private final BlockingQueue<String> workQueue;

    // 文件客户端实例，用于执行实际的文件下载操作
    private final FileClient fileClient;

    /**
     * 构造函数，初始化工作队列和文件客户端
     */
    public WorkThread() {
        // 创建容量为128的任务队列
        this.workQueue = new ArrayBlockingQueue<>(128);
        // 创建文件客户端实例
        this.fileClient = new FileClient();
        // 初始化文件客户端
        fileClient.initClient();
    }

    /**
     * 执行具体的文件下载任务
     * <p>从工作队列中取出文件名并调用文件客户端进行下载</p>
     *
     * @throws InterruptedException 当线程被中断时抛出
     */
    @Override
    protected void doRun() throws InterruptedException {
        // 从工作队列中取出待下载的文件名
        String file = this.workQueue.take();
        try {
            // 使用文件客户端下载文件
            fileClient.downloadFile(file);
        } finally {
            // 任务执行完成后减少未执行任务计数
            terminationToken.noExecuteTaskCount.decrementAndGet();
        }
    }

    /**
     * 提交文件下载任务到工作队列
     *
     * @param fileName 要下载的文件名
     */
    public void downloadFile(String fileName) {
        try {
            // 将文件名放入工作队列
            this.workQueue.put(fileName);
            // 增加未执行任务计数
            terminationToken.noExecuteTaskCount.incrementAndGet();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
