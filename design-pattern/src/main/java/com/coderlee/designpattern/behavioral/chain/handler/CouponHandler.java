package com.coderlee.designpattern.behavioral.chain.handler;

import com.coderlee.designpattern.behavioral.chain.Order;
import com.coderlee.designpattern.behavioral.chain.OrderHandler;
import com.coderlee.designpattern.behavioral.chain.OrderType;

import java.math.BigDecimal;

/**
 * 优惠券处理器
 * 职责：根据订单类型和金额，计算并应用优惠券
 */
public class CouponHandler extends OrderHandler {

    private static final BigDecimal GROUP_BUY_THRESHOLD = new BigDecimal("500");
    private static final BigDecimal PRE_SALE_THRESHOLD = new BigDecimal("1000");

    @Override
    protected boolean doHandle(Order order) {
        BigDecimal amount = order.getAmount();
        OrderType type = order.getOrderType();

        // 团购订单满 500 减 50
        if (type == OrderType.GROUP_BUY && amount.compareTo(GROUP_BUY_THRESHOLD) >= 0) {
            log("应用团购优惠：满 500 减 50");
            order.addLog("应用团购优惠：原价 " + amount + "，优惠 50");
            return true;
        }

        // 预售订单满 1000 减 100
        if (type == OrderType.PRE_SALE && amount.compareTo(PRE_SALE_THRESHOLD) >= 0) {
            log("应用预售优惠：满 1000 减 100");
            order.addLog("应用预售优惠：原价 " + amount + "，优惠 100");
            return true;
        }

        // 秒杀订单不使用优惠券
        if (type == OrderType.FLASH_SALE) {
            log("秒杀订单，不使用优惠券");
            order.addLog("秒杀订单，无优惠券");
            return true;
        }

        // 普通订单/海外购订单，没有适用优惠券，但仍然通过（不阻断流程）
        log("无适用优惠券，跳过");
        order.addLog("无适用优惠券");
        return true;
    }
}
