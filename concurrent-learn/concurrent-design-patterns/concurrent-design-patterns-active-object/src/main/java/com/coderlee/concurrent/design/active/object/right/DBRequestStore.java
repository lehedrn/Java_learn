package com.coderlee.concurrent.design.active.object.right;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * 基于数据库的请求存储实现
 * <p>
 * 该类实现了将商品请求信息持久化到数据库的操作，模拟了数据库存储的耗时操作
 * </p>
 */
@Slf4j
public class DBRequestStore implements RequestStore {

    /**
     * 将商品请求信息刷入数据库
     * <p>
     * 模拟数据库存储操作，包含1秒的处理延迟
     * </p>
     *
     * @param goodsRequest 商品请求对象
     * @see RequestStore#flush(GoodsRequest)
     * @see GoodsRequest
     */
    @Override
    public void flush(GoodsRequest goodsRequest) {
        // 记录开始存储操作
        log.info("保存请求到数据库开始");

        // 记录开始时间用于计算耗时
        Instant start = Instant.now();

        try {
            // 模拟数据库存储耗时操作
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            // 记录存储过程中的异常
            log.error("保存请求到数据库异常", e);
        }

        // 记录存储完成信息和数据内容
        log.info("保存请求到数据库完毕, 存储的数据为: {}", goodsRequest.toString());

        // 记录存储操作耗时
        log.info("保存请求到数据库耗时: {} ms", Duration.between(start, Instant.now()).toMillis());
    }

    /**
     * 关闭存储资源
     * <p>
     * 实现Closeable接口，用于释放存储相关的资源
     * </p>
     *
     * @throws IOException IO操作异常
     */
    @Override
    public void close() throws IOException {
        // 当前实现为空，可根据需要添加资源释放逻辑
    }
}
