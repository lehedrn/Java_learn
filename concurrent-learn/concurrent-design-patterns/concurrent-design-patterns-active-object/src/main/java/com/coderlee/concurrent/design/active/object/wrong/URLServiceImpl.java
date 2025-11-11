package com.coderlee.concurrent.design.active.object.wrong;

import com.coderlee.concurrent.design.active.object.common.URLTransfer;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class URLServiceImpl implements URLService {
    private static final ConcurrentMap<String, String> URL_MAP = new ConcurrentHashMap<>();
    @Override
    public String getShortUrl(String longUrl) {
        log.info("长链接转换短链接开始");
        Instant start = Instant.now();
        try {
            return URLTransfer.generateShortLink(longUrl);
        } finally {
            log.info("长链接转换短链接结束, 耗时: {} ms", Duration.between(start, Instant.now()).toMillis());
        }
    }

    @Override
    public void saveUrl(String shortUrl, String longUrl) {
        log.info("保存长短链接的映射关系开始");
        Instant start = Instant.now();
        try {
            TimeUnit.SECONDS.sleep(1);
            URL_MAP.put(shortUrl, longUrl);
            log.info("保存长短链接的映射关系, 短链接: {}, 长链接: {}", shortUrl, longUrl);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            log.info("保存长短链接的映射关系结束, 耗时: {} ms", Duration.between(start, Instant.now()).toMillis());
        }
    }

    @Override
    public String getLongUrlByShortUrl(String shortUrl) {
        return URL_MAP.get(shortUrl);
    }
}
