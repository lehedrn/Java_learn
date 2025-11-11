package com.coderlee.concurrent.design.promise.sync;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * {@link FileSyncer} 的具体实现类，模拟与远程服务器通信的过程。
 */
@Slf4j
public class FileSyncerImpl implements FileSyncer {

    /**
     * 建立与远程服务器的连接。
     *
     * @param serverAddress 远程服务器地址
     * @param username      用户名
     * @param password      密码
     * @param serverDir     目标服务器目录
     * @throws Exception 如果连接过程出现错误则抛出异常
     */
    @Override
    public void connect(String serverAddress, String username, String password, String serverDir) throws Exception {
        log.info("开始与远程服务器建立连接");
        Instant start = Instant.now();
        TimeUnit.SECONDS.sleep(2); // 模拟网络延迟
        log.info("与远程服务器建立连接成功, 耗时: {} ms", Duration.between(start, Instant.now()).toMillis());
    }

    /**
     * 将本地文件上传至远程服务器。
     *
     * @param fileSyncerInfo 待上传的文件信息对象
     * @throws Exception 如果上传过程出现错误则抛出异常
     */
    @Override
    public void uploadFile(FileSyncerInfo fileSyncerInfo) throws Exception {
        log.info("开始同步文件");
        Instant start = Instant.now();
        TimeUnit.SECONDS.sleep(3); // 模拟传输时间
        log.info("同步文件成功, 耗时: {} ms", Duration.between(start, Instant.now()).toMillis());
    }

    /**
     * 断开与远程服务器的连接。
     */
    @Override
    public void disconnect() {
        log.info("关闭与远程服务器的连接");
    }
}

