package com.coderlee.designpattern.behavioral.observer.mq;

/**
 * 支付成功消息
 * <p>
 * 支付成功后发送的消息
 * </p>
 *
 * @author coderlee
 */
public class PaymentSuccessMessage implements Message {

    /**
     * 消息类型
     */
    private static final String TYPE = "PAYMENT_SUCCESS";

    /**
     * 订单 ID
     */
    private final String orderId;

    /**
     * 用户 ID
     */
    private final String userId;

    /**
     * 支付金额
     */
    private final double amount;

    /**
     * 支付方式
     */
    private final String paymentMethod;

    /**
     * 时间戳
     */
    private final long timestamp;

    public PaymentSuccessMessage(String orderId, String userId, double amount, String paymentMethod) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getContent() {
        return String.format("订单 %s 支付成功，金额￥%.2f，支付方式：%s", orderId, amount, paymentMethod);
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
