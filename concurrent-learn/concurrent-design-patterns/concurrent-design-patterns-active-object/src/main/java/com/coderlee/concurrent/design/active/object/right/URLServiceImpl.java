package com.coderlee.concurrent.design.active.object.right;

import lombok.extern.slf4j.Slf4j;

/**
 * URL服务默认实现
 * <p>
 * 实现长链接到短链接的转换逻辑，当转换失败时采用异步方式存储请求信息
 * </p>
 */
@Slf4j
public class URLServiceImpl implements URLService {

    /**
     * 根据商品请求获取短链接
     * <p>
     * 尝试将商品请求中的长链接转换为短链接，转换失败时异步存储请求信息
     * </p>
     *
     * @param goodsRequest 商品请求对象
     * @return 生成的短链接，转换失败时返回null
     * @see URLService#getShortUrlByGoodsRequest(GoodsRequest)
     * @see ProxyRequestStore
     */
    @Override
    public String getShortUrlByGoodsRequest(GoodsRequest goodsRequest) {
        // 初始化短链接为null
        String shortUrl = null;

        try {
            // 尝试转换长链接为短链接
            shortUrl = getShortUrlByLongUrl(goodsRequest.getLongUrl());
        } catch (Exception e) {
            // 记录转换失败的异常信息
            log.error(e.getMessage());

            // 记录开始执行异步存储操作
            log.info("执行异步存储请求的操作");

            // 异步存储商品请求信息
            ProxyRequestStore.getInstance().flush(goodsRequest);
        }

        // 返回短链接结果
        return shortUrl;
    }

    /**
     * 根据长链接获取短链接
     * <p>
     * 模拟长链接转换短链接的操作，当前实现总是抛出异常
     * </p>
     *
     * @param longUrl 长链接
     * @return 短链接
     * @throws RuntimeException 转换失败异常
     */
    private String getShortUrlByLongUrl(String longUrl) {
        // 模拟转换失败，抛出运行时异常
        throw new RuntimeException("转换短链接失败, 当前传递的长链接为: " + longUrl);
    }
}
