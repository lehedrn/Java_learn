package com.coderlee.concurrent.design.thread.close.wrong;

import com.coderlee.concurrent.design.thread.close.common.FileClient;

/**
 * 错误示例的文件服务实现类
 * <p>直接在调用线程中执行文件下载，没有专门的工作线程管理</p>
 *
 * @see com.coderlee.concurrent.design.thread.close.right.FileServiceImpl 正确的实现方式
 */
public class FileServiceImpl implements FileService {

    // 文件客户端实例
    private FileClient fileClient;

    /**
     * 构造函数，创建文件客户端实例
     */
    public FileServiceImpl() {
        this.fileClient = new FileClient();
    }

    /**
     * 直接在当前线程中下载文件
     *
     * @param fileName 要下载的文件名
     */
    @Override
    public void downloadFile(String fileName) {
        fileClient.downloadFile(fileName);
    }
}
