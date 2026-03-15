package com.coderlee.designpattern.behavioral.chain.captive.handler;

import com.coderlee.designpattern.behavioral.chain.captive.PaymentChannel;
import com.coderlee.designpattern.behavioral.chain.captive.PaymentContext;
import com.coderlee.designpattern.behavioral.chain.captive.PaymentHandler;

/**
 * 微信支付处理器
 * 优先级：高（用户首选）
 */
public class WechatPayHandler extends PaymentHandler {

    @Override
    protected boolean canHandle(PaymentContext context) {
        // 用户首选微信支付
        boolean isPreferred = context.getPreferredChannel() == PaymentChannel.WECHAT_PAY;
        boolean withinLimit = context.getAmount().compareTo(new java.math.BigDecimal("50000")) <= 0;

        if (isPreferred && withinLimit) {
            log("用户首选微信支付，金额在限额内");
            return true;
        }
        return false;
    }

    @Override
    protected boolean doHandle(PaymentContext context) {
        log("正在调用微信支付接口...");
        log("✓ 微信支付成功");
        context.setResult("微信支付成功，金额：" + context.getAmount());
        return true;
    }
}
