package com.coderlee.concurrent.design.half.sync.async.wrong;

import java.math.BigDecimal;

/**
 * 错误示例中的支付服务接口。
 * <p>
 * 和正确版本功能相同，但其具体实现在性能上存在问题。
 * </p>
 */
public interface PayService {

    /**
     * 执行支付操作。
     *
     * @param money 支付金额
     */
    void pay(BigDecimal money);
}

