package com.coderlee.concurrent.design.thread.close.wrong;

/**
 * 错误示例的文件服务接口
 * <p>仅提供基本的文件下载功能，没有线程管理机制</p>
 *
 * @see com.coderlee.concurrent.design.thread.close.right.FileService 正确的实现方式
 */
public interface FileService {

    /**
     * 下载指定文件
     *
     * @param fileName 要下载的文件名
     */
    void downloadFile(String fileName);
}
