package com.coderlee.concurrent.design.thread.close.right;

/**
 * 文件服务接口
 * <p>定义了文件下载服务的基本操作，包括下载文件、初始化和关闭服务</p>
 *
 * @see FileServiceImpl
 */
public interface FileService {

    /**
     * 下载指定文件
     *
     * @param fileName 要下载的文件名
     */
    void downloadFile(String fileName);

    /**
     * 初始化文件服务
     */
    void init();

    /**
     * 关闭文件服务
     */
    void shutdown();
}
