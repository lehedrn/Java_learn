package com.coderlee.designpattern.behavioral.strategy.advanced.enumstrategy;

/**
 * 策略接口：运费计算策略
 * <p>
 * 定义所有快递公司的运费计算接口
 * </p>
 *
 * @author coderlee
 */
public interface ShippingStrategy {
    /**
     * 计算运费
     *
     * @param weight 重量（kg）
     * @param distance 距离（km）
     * @return 运费
     */
    double calculateShippingFee(double weight, double distance);

    /**
     * 获取快递公司名称
     *
     * @return 快递公司名称
     */
    String getCourierName();

    /**
     * 预计送达天数
     *
     * @param distance 距离
     * @return 预计天数
     */
    int estimateDeliveryDays(double distance);
}
