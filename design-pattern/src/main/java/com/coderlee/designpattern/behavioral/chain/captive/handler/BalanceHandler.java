package com.coderlee.designpattern.behavioral.chain.captive.handler;

import com.coderlee.designpattern.behavioral.chain.captive.PaymentChannel;
import com.coderlee.designpattern.behavioral.chain.captive.PaymentContext;
import com.coderlee.designpattern.behavioral.chain.captive.PaymentHandler;

/**
 * 余额支付处理器
 * 优先级：低（兜底方案）
 */
public class BalanceHandler extends PaymentHandler {

    private static final java.math.BigDecimal MAX_BALANCE_AMOUNT = new java.math.BigDecimal("5000");

    @Override
    protected boolean canHandle(PaymentContext context) {
        // 余额支付作为兜底，金额不超过 5000 时使用
        boolean withinLimit = context.getAmount().compareTo(MAX_BALANCE_AMOUNT) <= 0;

        if (withinLimit) {
            log("检查账户余额...");
            // 模拟余额充足
            log("账户余额充足");
            return true;
        }

        log("金额超过余额支付限额");
        return false;
    }

    @Override
    protected boolean doHandle(PaymentContext context) {
        log("正在扣减账户余额...");
        log("✓ 余额支付成功");
        context.setResult("余额支付成功，金额：" + context.getAmount());
        return true;
    }
}
