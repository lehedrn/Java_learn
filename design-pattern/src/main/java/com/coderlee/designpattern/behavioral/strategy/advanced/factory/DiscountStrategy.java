package com.coderlee.designpattern.behavioral.strategy.advanced.factory;

/**
 * 策略接口：折扣策略
 * <p>
 * 定义所有折扣策略的公共接口
 * </p>
 *
 * @author coderlee
 */
public interface DiscountStrategy {
    /**
     * 计算折扣后的价格
     *
     * @param originalPrice 原价
     * @return 折扣后价格
     */
    double calculateDiscountedPrice(double originalPrice);

    /**
     * 获取折扣策略名称
     *
     * @return 折扣策略名称
     */
    String getDiscountName();
}
