package com.coderlee.designpattern.behavioral.chain.captive;

import com.coderlee.designpattern.behavioral.chain.captive.handler.*;

/**
 * 支付处理器链构建器
 */
public class PaymentProcessorChain {

    private final PaymentHandler head;

    private PaymentProcessorChain(PaymentHandler head) {
        this.head = head;
    }

    /**
     * 构建标准支付链
     * 顺序：支付宝 → 微信支付 → 银联 → 余额 → 组合支付
     */
    public static PaymentProcessorChain buildStandardChain() {
        PaymentHandler alipay = new AlipayHandler();
        PaymentHandler wechat = new WechatPayHandler();
        PaymentHandler unionPay = new UnionPayHandler();
        PaymentHandler balance = new BalanceHandler();
        PaymentHandler combo = new ComboPayHandler();

        alipay.setNext(wechat)
              .setNext(unionPay)
              .setNext(balance)
              .setNext(combo);

        return new PaymentProcessorChain(alipay);
    }

    /**
     * 构建快捷支付链（去掉组合支付）
     * 顺序：支付宝 → 微信支付 → 银联
     */
    public static PaymentProcessorChain buildFastChain() {
        PaymentHandler alipay = new AlipayHandler();
        PaymentHandler wechat = new WechatPayHandler();
        PaymentHandler unionPay = new UnionPayHandler();

        alipay.setNext(wechat).setNext(unionPay);

        return new PaymentProcessorChain(alipay);
    }

    /**
     * 构建大额支付链（银联前置）
     * 顺序：银联 → 组合支付 → 支付宝
     */
    public static PaymentProcessorChain buildHighAmountChain() {
        PaymentHandler unionPay = new UnionPayHandler();
        PaymentHandler combo = new ComboPayHandler();
        PaymentHandler alipay = new AlipayHandler();

        unionPay.setNext(combo).setNext(alipay);

        return new PaymentProcessorChain(unionPay);
    }

    /**
     * 处理支付
     * @return true 表示支付成功，false 表示失败
     */
    public boolean process(PaymentContext context) {
        return head.handle(context);
    }
}
