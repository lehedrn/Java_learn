package com.coderlee.designpattern.behavioral.chain.captive;

/**
 * 支付上下文
 */
public class PaymentContext {

    private final String orderId;
    private final String userId;
    private final java.math.BigDecimal amount;
    private final PaymentChannel preferredChannel;
    private String result;

    public PaymentContext(String orderId, String userId, java.math.BigDecimal amount, PaymentChannel preferredChannel) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.preferredChannel = preferredChannel;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public PaymentChannel getPreferredChannel() {
        return preferredChannel;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "订单=" + orderId + ", 金额=" + amount + ", 首选渠道=" + preferredChannel;
    }
}
