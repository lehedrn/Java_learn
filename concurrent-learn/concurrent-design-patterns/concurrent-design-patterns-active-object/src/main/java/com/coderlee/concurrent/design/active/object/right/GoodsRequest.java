package com.coderlee.concurrent.design.active.object.right;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品请求数据传输对象
 * <p>
 * 该类用于封装商品长链接信息，作为Active Object模式中的参数对象
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRequest {
    /**
     * 商品长链接URL
     */
    private String longUrl;
}
