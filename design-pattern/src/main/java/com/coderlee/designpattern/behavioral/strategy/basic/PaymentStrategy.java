package com.coderlee.designpattern.behavioral.strategy.basic;

/**
 * 策略接口：支付方式
 * <p>
 * 定义所有支付方式的公共接口
 * </p>
 *
 * @author coderlee
 */
public interface PaymentStrategy {
    /**
     * 支付方法
     *
     * @param amount 支付金额
     * @return 是否支付成功
     */
    boolean pay(double amount);

    /**
     * 获取支付方式名称
     *
     * @return 支付方式名称
     */
    String getPaymentMethod();
}
