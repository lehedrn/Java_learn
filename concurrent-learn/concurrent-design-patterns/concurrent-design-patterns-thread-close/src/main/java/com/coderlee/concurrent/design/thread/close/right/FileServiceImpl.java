package com.coderlee.concurrent.design.thread.close.right;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件服务实现类
 * <p>正确实现线程管理的文件服务，使用工作线程处理文件下载任务</p>
 *
 * @see FileService
 * @see WorkThread
 */
@Slf4j
public class FileServiceImpl implements FileService {

    // 工作线程实例，负责实际的文件下载任务
    private WorkThread workThread;

    /**
     * 构造函数，创建并初始化工作线程
     */
    public FileServiceImpl() {
        workThread = new WorkThread();
    }

    /**
     * 提交文件下载任务到工作线程
     *
     * @param fileName 要下载的文件名
     */
    @Override
    public void downloadFile(String fileName) {
        // 记录任务提交日志
        log.info("{} 线程提交任务", Thread.currentThread().getName());
        workThread.downloadFile(fileName);
    }

    /**
     * 初始化并启动工作线程
     */
    @Override
    public void init() {
        workThread.start();
    }

    /**
     * 关闭工作线程
     */
    @Override
    public void shutdown() {
        workThread.terminate();
    }
}
