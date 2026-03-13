package com.coderlee.designpattern.behavioral.strategy.basic;

/**
 * 具体策略：微信支付
 *
 * @author coderlee
 */
public class WechatPayStrategy implements PaymentStrategy {

    /**
     * 微信 OpenID
     */
    private final String wechatOpenId;

    public WechatPayStrategy(String wechatOpenId) {
        this.wechatOpenId = wechatOpenId;
    }

    @Override
    public boolean pay(double amount) {
        System.out.printf("💳 微信支付：OpenID %s 支付 ￥%.2f\n", wechatOpenId, amount);
        // 模拟支付处理
        System.out.println("   → 正在调用微信支付 API...");
        System.out.println("   → 发送支付请求到用户微信...");
        System.out.println("   → 用户确认支付成功！");
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "微信";
    }
}
