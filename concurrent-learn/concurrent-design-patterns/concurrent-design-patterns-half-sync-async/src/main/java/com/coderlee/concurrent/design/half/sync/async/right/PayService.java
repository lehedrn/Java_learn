package com.coderlee.concurrent.design.half.sync.async.right;

import java.math.BigDecimal;

/**
 * 支付服务接口。
 * <p>
 * 定义了一个支付操作的标准契约。
 * </p>
 */
public interface PayService {

    /**
     * 执行支付操作。
     *
     * @param bigDecimal 支付金额
     */
    void pay(BigDecimal bigDecimal);
}

