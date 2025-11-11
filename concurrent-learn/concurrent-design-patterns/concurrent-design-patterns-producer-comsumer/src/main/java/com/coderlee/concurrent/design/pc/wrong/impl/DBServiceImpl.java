package com.coderlee.concurrent.design.pc.wrong.impl;

import com.coderlee.concurrent.design.pc.wrong.service.DBService;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;

/**
 * 数据库服务实现类
 *
 * <p>实现了 {@link DBService} 接口，提供了保存业务数据的具体实现。</p>
 *
 * @see DBService
 */
@Slf4j
public class DBServiceImpl implements DBService {

    /**
     * 保存业务数据的实现方法
     *
     * <p>记录日志并模拟保存业务数据的过程。</p>
     *
     * @param businessData 要保存的业务数据
     */
    @Override
    public void save(String businessData) {
        // 记录保存业务数据开始
        log.info("保存业务数据开始");
        Instant start = Instant.now();

        // 模拟保存业务数据逻辑
        log.info("保存业务数据: {} 成功，耗时: {} ms", businessData, Duration.between(start, Instant.now()).toMillis());
    }
}
