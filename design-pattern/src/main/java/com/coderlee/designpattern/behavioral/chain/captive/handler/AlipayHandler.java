package com.coderlee.designpattern.behavioral.chain.captive.handler;

import com.coderlee.designpattern.behavioral.chain.captive.PaymentChannel;
import com.coderlee.designpattern.behavioral.chain.captive.PaymentContext;
import com.coderlee.designpattern.behavioral.chain.captive.PaymentHandler;

/**
 * 支付宝处理器
 * 优先级：最高（用户首选）
 */
public class AlipayHandler extends PaymentHandler {

    @Override
    protected boolean canHandle(PaymentContext context) {
        // 用户首选支付宝，且订单金额在限额内
        boolean isPreferred = context.getPreferredChannel() == PaymentChannel.ALIPAY;
        boolean withinLimit = context.getAmount().compareTo(new java.math.BigDecimal("50000")) <= 0;

        if (isPreferred && withinLimit) {
            log("用户首选支付宝，金额在限额内");
            return true;
        }

        // 支付宝余额不足时不使用
        if (isPreferred) {
            log("支付宝限额超限");
        }
        return false;
    }

    @Override
    protected boolean doHandle(PaymentContext context) {
        log("正在调用支付宝支付接口...");
        log("✓ 支付宝支付成功");
        context.setResult("支付宝支付成功，金额：" + context.getAmount());
        return true;
    }
}
