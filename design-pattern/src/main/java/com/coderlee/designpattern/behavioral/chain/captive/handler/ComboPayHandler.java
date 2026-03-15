package com.coderlee.designpattern.behavioral.chain.captive.handler;

import com.coderlee.designpattern.behavioral.chain.captive.PaymentChannel;
import com.coderlee.designpattern.behavioral.chain.captive.PaymentContext;
import com.coderlee.designpattern.behavioral.chain.captive.PaymentHandler;

/**
 * 组合支付处理器
 * 优先级：特殊场景（当单一渠道无法完成时）
 * 例如：余额 + 支付宝 组合支付
 */
public class ComboPayHandler extends PaymentHandler {

    @Override
    protected boolean canHandle(PaymentContext context) {
        // 组合支付作为最后的兜底方案
        // 这里简化处理：只要金额不超过 100000 就支持组合支付
        boolean withinLimit = context.getAmount().compareTo(new java.math.BigDecimal("100000")) <= 0;

        if (withinLimit) {
            log("启用组合支付方案（支付宝 + 余额）");
            return true;
        }

        return false;
    }

    @Override
    protected boolean doHandle(PaymentContext context) {
        log("正在处理组合支付...");
        log("  - 支付宝部分：调用支付宝接口");
        log("  - 余额部分：扣减账户余额");
        log("✓ 组合支付成功");
        context.setResult("组合支付成功，金额：" + context.getAmount());
        return true;
    }
}
