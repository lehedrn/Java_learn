package com.coderlee.concurrent.design.promise.sync;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 文件同步主任务类，实现了Runnable接口，在独立线程中运行整个同步流程。
 */
@Data
@Slf4j
public class FileSyncerTask implements Runnable {

    // 存储配置信息
    private final FileSyncerConfig fileSyncerConfig;

    /**
     * 执行同步逻辑的主要入口。
     */
    @Override
    public void run() {
        log.info("同步文件总体开始");
        Instant start = Instant.now();
        FileSyncer fileSyncer = null;
        try {

            // 异步获取已连接的FileSyncer实例
            Future<FileSyncer> promise = FileSyncerPromisor.getINSTANCE().execute(fileSyncerConfig);

            // 扫描本地文件
            List<FileSyncerInfo> fileSyncerInfoList = FileSyncerScanner.getInstance().scanFile("/home/workspace/coderlee");

            // 判断是否为空
            if (null == fileSyncerInfoList || fileSyncerInfoList.isEmpty()) {
                log.info("扫描的文件为空");
                return;
            }

            // 获取FileSyncer实例
            fileSyncer = promise.get();
            if (null == fileSyncer) {
                log.info("连接远程服务器失败");
                return;
            }

            // 开始上传文件
            this.uploadFile(fileSyncer, fileSyncerInfoList);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            // 最终断开连接
            if (null != fileSyncer) {
                fileSyncer.disconnect();
            }
        }
        log.info("同步文件结束，总体耗时: {} ms", Duration.between(start, Instant.now()).toMillis());
    }

    /**
     * 逐个上传文件列表中的每一个文件。
     *
     * @param fileSyncer         已经连接好的FileSyncer实例
     * @param fileSyncerInfoList 待上传的文件列表
     */
    private void uploadFile(FileSyncer fileSyncer, List<FileSyncerInfo> fileSyncerInfoList){
        fileSyncerInfoList.forEach((fileSyncerInfo) -> {
            try {
                fileSyncer.uploadFile(fileSyncerInfo); // 上传当前文件
            } catch (Exception e) {
                log.error("上传文件失败", e); // 记录错误日志
            }
        });
    }

}
