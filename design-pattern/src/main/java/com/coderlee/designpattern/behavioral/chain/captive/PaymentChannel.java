package com.coderlee.designpattern.behavioral.chain.captive;

/**
 * 支付渠道枚举
 */
public enum PaymentChannel {
    /**
     * 支付宝
     */
    ALIPAY("支付宝"),

    /**
     * 微信支付
     */
    WECHAT_PAY("微信支付"),

    /**
     * 银联
     */
    UNION_PAY("银联"),

    /**
     * 数字人民币
     */
    DIGITAL_CNY("数字人民币"),

    /**
     * 银行卡
     */
    BANK_CARD("银行卡"),

    /**
     * 余额支付
     */
    BALANCE("余额"),

    /**
     * 组合支付（多个渠道）
     */
    COMBO("组合支付");

    private final String displayName;

    PaymentChannel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
