package com.coderlee.concurrent.design.active.object.right;

import java.io.Closeable;

/**
 * 请求存储服务接口
 * <p>
 * 定义了将商品请求信息持久化的操作方法
 * </p>
 */
public interface RequestStore extends Closeable {
    /**
     * 将商品请求信息刷入存储介质
     *
     * @param goodsRequest 商品请求对象
     * @see GoodsRequest
     */
    void flush(GoodsRequest goodsRequest);
}
