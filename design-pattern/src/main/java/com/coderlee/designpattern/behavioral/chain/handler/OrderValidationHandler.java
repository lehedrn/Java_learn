package com.coderlee.designpattern.behavioral.chain.handler;

import com.coderlee.designpattern.behavioral.chain.Order;
import com.coderlee.designpattern.behavioral.chain.OrderHandler;

/**
 * 订单验证处理器
 * 职责：验证订单的基本信息是否合法
 * 如果验证失败，整个流程终止
 */
public class OrderValidationHandler extends OrderHandler {

    @Override
    protected boolean doHandle(Order order) {
        // 验证订单 ID
        if (order.getOrderId() == null || order.getOrderId().isEmpty()) {
            log("验证失败：订单 ID 为空");
            return false;
        }

        // 验证金额
        if (order.getAmount() == null || order.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            log("验证失败：订单金额非法");
            return false;
        }

        // 验证订单类型
        if (order.getOrderType() == null) {
            log("验证失败：订单类型为空");
            return false;
        }

        log("✓ 订单验证通过");
        order.addLog("验证通过");
        return true;
    }
}
