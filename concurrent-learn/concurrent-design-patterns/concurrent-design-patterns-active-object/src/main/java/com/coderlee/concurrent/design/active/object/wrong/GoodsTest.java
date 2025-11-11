package com.coderlee.concurrent.design.active.object.wrong;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;

@Slf4j
public class GoodsTest {
    public static void main(String[] args) {
        log.info("整体任务执行开始");
        Instant start = Instant.now();
        Goods goods = new Goods(1001L, "小米手机15系列", "https://www.mi.com/shop/buy/detail?product_id=10050081");
        GoodsService goodsService = new GoodsServiceImpl();
        String shortUrl = goodsService.saveGoods(goods);
        URLService urlService = new URLServiceImpl();
        String longUrl = urlService.getLongUrlByShortUrl(shortUrl);
        log.info("获取到的长链接是: {}", longUrl);
        log.info("整体任务执行结束, 耗时: {} ms", Duration.between(start, Instant.now()).toMillis());
    }
}
