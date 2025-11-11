package com.coderlee.concurrent.design.promise.sync;

/**
 * 文件同步接口，定义了与远程服务器交互的基本操作。
 */
public interface FileSyncer {

    /**
     * 连接到指定的远程服务器。
     *
     * @param serverAddress 远程服务器地址
     * @param username      用户名
     * @param password      密码
     * @param serverDir     目标服务器目录
     * @throws Exception 如果连接过程中发生错误，则抛出异常
     */
    void connect(String serverAddress, String username, String password, String serverDir) throws Exception;

    /**
     * 上传文件到已连接的远程服务器。
     *
     * @param fileSyncerInfo 待上传的文件信息对象
     * @throws Exception 如果上传过程中发生错误，则抛出异常
     */
    void uploadFile(FileSyncerInfo fileSyncerInfo) throws Exception;

    /**
     * 断开与远程服务器的连接。
     */
    void disconnect();
}

