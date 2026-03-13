package com.coderlee.designpattern.behavioral.strategy.basic;

/**
 * 具体策略：支付宝支付
 *
 * @author coderlee
 */
public class AlipayStrategy implements PaymentStrategy {

    /**
     * 支付宝账号
     */
    private final String alipayAccount;

    public AlipayStrategy(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    @Override
    public boolean pay(double amount) {
        System.out.printf("💳 支付宝支付：账号 %s 支付 ￥%.2f\n", alipayAccount, amount);
        // 模拟支付处理
        System.out.println("   → 正在连接支付宝网关...");
        System.out.println("   → 验证用户身份...");
        System.out.println("   → 扣款成功！");
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "支付宝";
    }
}
