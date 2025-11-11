package com.coderlee.concurrent.design.pc.wrong.impl;

import com.coderlee.concurrent.design.pc.wrong.service.UploadService;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

/**
 * 文件上传服务实现类
 *
 * <p>实现了 {@link UploadService} 接口，提供了上传文件的具体实现。</p>
 *
 * @see UploadService
 */
@Slf4j
public class UploadServiceImpl implements UploadService {

    /**
     * 上传文件的实现方法
     *
     * <p>记录日志并模拟上传文件的过程。</p>
     *
     * @param fileNames 要上传的文件名数组
     */
    @Override
    public void upload(String... fileNames) {
        // 记录上传文件开始
        log.info("上传文件开始");
        Instant start = Instant.now();

        // 记录上传成功的文件信息
        log.info("上传文件成功, 上传的文件如下所示: ");
        Arrays.stream(fileNames).forEach(fileName -> log.info("文件名: {}", fileName));

        // 记录上传文件结束及耗时
        log.info("上传文件结束, 耗时: {} ms", Duration.between(start, Instant.now()).toMillis());
    }
}
