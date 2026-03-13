package com.coderlee.designpattern.behavioral.strategy.basic;

/**
 * 具体策略：银行卡支付
 *
 * @author coderlee
 */
public class BankCardStrategy implements PaymentStrategy {

    /**
     * 银行卡号
     */
    private final String cardNumber;

    /**
     * 银行名称
     */
    private final String bankName;

    public BankCardStrategy(String bankName, String cardNumber) {
        this.bankName = bankName;
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean pay(double amount) {
        System.out.printf("💳 银行卡支付：%s %s 支付 ￥%.2f\n",
                bankName, maskCardNumber(cardNumber), amount);
        // 模拟支付处理
        System.out.println("   → 正在连接银联网关...");
        System.out.println("   → 验证银行卡信息...");
        System.out.println("   → 扣款成功！");
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "银行卡";
    }

    /**
     * 脱敏银行卡号
     */
    private String maskCardNumber(String cardNumber) {
        if (cardNumber.length() < 8) {
            return "****";
        }
        return "****" + cardNumber.substring(cardNumber.length() - 4);
    }
}
