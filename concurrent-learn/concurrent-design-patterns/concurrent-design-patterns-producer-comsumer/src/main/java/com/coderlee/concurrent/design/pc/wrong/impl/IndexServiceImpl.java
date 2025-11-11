package com.coderlee.concurrent.design.pc.wrong.impl;

import com.coderlee.concurrent.design.pc.wrong.service.IndexService;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * 索引服务实现类
 *
 * <p>实现了 {@link IndexService} 接口，提供了为文件建立索引的具体实现。</p>
 *
 * @see IndexService
 */
@Slf4j
public class IndexServiceImpl implements IndexService {

    /**
     * 为文件建立索引的实现方法
     *
     * <p>记录日志并模拟为文件建立索引的过程，其中包含5秒的延迟模拟实际耗时操作。</p>
     *
     * @param fileNames 要建立索引的文件名数组
     */
    @Override
    public void index(String... fileNames) {
        // 记录索引文件数据开始
        log.info("索引文件数据开始");
        Instant start = Instant.now();

        try {
            // 模拟建立索引的耗时操作（休眠5秒）
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 记录索引文件数据结束及耗时
        log.info("索引文件数据结束，耗时: {} ms", Duration.between(start, Instant.now()).toMillis());
    }
}
