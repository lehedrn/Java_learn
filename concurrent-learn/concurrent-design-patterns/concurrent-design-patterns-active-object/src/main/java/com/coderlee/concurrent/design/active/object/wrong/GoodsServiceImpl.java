package com.coderlee.concurrent.design.active.object.wrong;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GoodsServiceImpl implements GoodsService {
    private URLService urlService = new URLServiceImpl();
    @Override
    public String saveGoods(Goods goods) {
        this.saveGoodsToDb(goods);
        String longUrl = goods.getLongUrl();
        String shortUrl = urlService.getShortUrl(longUrl);
        urlService.saveUrl(shortUrl, longUrl);
        return shortUrl;
    }
    private void saveGoodsToDb(Goods goods) {
        log.info("保存商品到数据库开始");
        Instant start = Instant.now();
        try {
            TimeUnit.SECONDS.sleep(2);
            log.info("保存商品到数据库完毕, 保存的数据为: {}, 耗时: {} ms", goods.toString(), Duration.between(start, Instant.now()).toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
