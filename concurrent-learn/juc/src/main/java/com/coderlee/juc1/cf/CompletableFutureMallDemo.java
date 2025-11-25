package com.coderlee.juc1.cf;

import com.coderlee.juc1.utils.CostTimeUtils;
import com.coderlee.juc1.utils.SleepUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 电商网站的比价
 * 演示使用传统方式和CompletableFuture并行处理方式查询多个商城的商品价格
 * 展示CompletableFuture在I/O密集型任务中的性能优势
 * 详细需求说明见 docs/juc1/02/案例精讲——电商网站的比价.md
 */
@Slf4j
public class CompletableFutureMallDemo {
    // 初始化商城列表，模拟不同的电商平台
    static List<NetMall> list = Arrays.asList(
            new NetMall("jd"),       // 京东商城
            new NetMall("taobao"),   // 淘宝商城
            new NetMall("dangdang"), // 当当商城
            new NetMall("pdd"),      // 拼多多商城
            new NetMall("tmall")     // 天猫商城
    );

    /**
     * 串行方式获取商品在各商城的价格
     * 依次查询每个商城的价格，总耗时为各商城查询时间之和
     *
     * @param list 商城列表
     * @param productName 商品名称
     * @return 各商城价格信息列表
     */
    public static List<String> getPrice(List<NetMall> list, String productName) {
        return list.stream()
                // 依次调用每个商城的calcPrice方法计算价格
                .map(netMall -> String.format(productName + " in %s price is %.2f",
                        netMall.getNetMallName(),
                        netMall.calcPrice(productName)))
                .collect(Collectors.toList());
    }

    /**
     * 使用CompletableFuture并行获取商品在各商城的价格
     * 并行查询各商城价格，总耗时约等于单个商城查询时间
     *
     * @param list 商城列表
     * @param productName 商品名称
     * @return 各商城价格信息列表
     */
    public static List<String> getPriceByCompletableFuture(List<NetMall> list, String productName) {
        return list.stream()
                // 为每个商城创建异步任务并行执行价格计算
                .map(netMall ->
                        CompletableFuture.supplyAsync(() ->
                                String.format(productName + " in %s price is %.2f",
                                        netMall.getNetMallName(),
                                        netMall.calcPrice(productName))))
                .collect(Collectors.toList())
                .stream()
                // 等待所有异步任务完成并获取结果
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    /**
     * 主方法，演示两种价格查询方式的性能差异
     */
    public static void main(String[] args) {
        CostTimeUtils.calcCostTime(unused -> {
            List<String> list_1 = getPrice(list, "mysql");
            for (String ele : list_1) {
                log.info(ele);
            }
        }, "getPrice");

        CostTimeUtils.calcCostTime(unused -> {
            List<String> list_2 = getPriceByCompletableFuture(list, "mysql");
            for (String ele : list_2) {
                log.info(ele);
            }
        }, "getPriceByCompletableFuture");

    }
}

/**
 * 网络商城类
 * 模拟电商网站，提供商品价格计算功能
 */
@AllArgsConstructor
class NetMall {
    @Getter
    private String netMallName; // 商城名称

    /**
     * 计算商品价格
     * 模拟网络延迟和价格计算过程
     *
     * @param productName 商品名称
     * @return 计算后的商品价格
     */
    public double calcPrice(String productName) {
        SleepUtils.sleep(1000); // 模拟网络延迟1秒
        // 模拟价格计算：随机数(0-2) + 商品首字母ASCII值
        return ThreadLocalRandom.current().nextDouble() * 2 + productName.charAt(0);
    }
}
