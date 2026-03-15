package com.coderlee.designpattern.behavioral.chain.captive.handler;

import com.coderlee.designpattern.behavioral.chain.captive.PaymentChannel;
import com.coderlee.designpattern.behavioral.chain.captive.PaymentContext;
import com.coderlee.designpattern.behavioral.chain.captive.PaymentHandler;

import java.math.BigDecimal;

/**
 * 银联处理器
 * 优先级：中（大额支付首选）
 */
public class UnionPayHandler extends PaymentHandler {

    private static final BigDecimal HIGH_AMOUNT_THRESHOLD = new BigDecimal("10000");

    @Override
    protected boolean canHandle(PaymentContext context) {
        // 用户首选银联，或者金额较大（>10000）且用户没有明确排斥银联
        boolean isPreferred = context.getPreferredChannel() == PaymentChannel.UNION_PAY;
        boolean isHighAmount = context.getAmount().compareTo(HIGH_AMOUNT_THRESHOLD) > 0;

        if (isPreferred) {
            log("用户首选银联");
            return true;
        }

        if (isHighAmount && context.getPreferredChannel() != PaymentChannel.BALANCE) {
            log("大额支付，推荐银联");
            return true;
        }

        return false;
    }

    @Override
    protected boolean doHandle(PaymentContext context) {
        log("正在调用银联支付接口...");
        log("✓ 银联支付成功");
        context.setResult("银联支付成功，金额：" + context.getAmount());
        return true;
    }
}
