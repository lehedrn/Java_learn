package com.coderlee.concurrent.design.active.object.right;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;

/**
 * 商品服务测试程序
 * <p>
 * 演示Active Object模式在商品请求处理中的应用，
 * 包括短链接生成和异步请求存储的完整流程
 * </p>
 */
@Slf4j
public class GoodsTest {

    /**
     * 程序入口点
     * <p>
     * 执行商品请求处理流程，包括短链接生成和性能统计
     * </p>
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 记录总体任务开始
        log.info("总体任务开始");

        // 记录开始时间用于计算总耗时
        Instant start = Instant.now();

        // 创建商品请求对象
        GoodsRequest goodsRequest = new GoodsRequest("https://www.mi.com/shop/buy/detail?product_id=10050081");

        // 创建URL服务实例
        URLService urlService = new URLServiceImpl();

        // 获取短链接（此处会失败并触发异步存储）
        String shortUrl = urlService.getShortUrlByGoodsRequest(goodsRequest);

        // 记录生成的短链接
        log.info("短链接: {}", shortUrl);

        // 记录总体任务结束和总耗时
        log.info("总体任务结束, 耗时: {} ms", Duration.between(start, Instant.now()).toMillis());
    }
}
