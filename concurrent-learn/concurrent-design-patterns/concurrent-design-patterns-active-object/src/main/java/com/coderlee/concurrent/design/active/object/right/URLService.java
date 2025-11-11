package com.coderlee.concurrent.design.active.object.right;

/**
 * URL服务接口
 * <p>
 * 提供长链接转换为短链接的服务接口定义
 * </p>
 */
public interface URLService {
    /**
     * 根据商品请求获取短链接
     *
     * @param goodsRequest 商品请求对象
     * @return 生成的短链接
     * @see GoodsRequest
     */
    String getShortUrlByGoodsRequest(GoodsRequest goodsRequest);
}
