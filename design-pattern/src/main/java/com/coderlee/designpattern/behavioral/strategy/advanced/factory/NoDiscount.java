package com.coderlee.designpattern.behavioral.strategy.advanced.factory;

/**
 * 具体策略：无折扣（原价）
 *
 * @author coderlee
 */
public class NoDiscount implements DiscountStrategy {

    @Override
    public double calculateDiscountedPrice(double originalPrice) {
        System.out.printf("   无折扣：￥%.2f\n", originalPrice);
        return originalPrice;
    }

    @Override
    public String getDiscountName() {
        return "无折扣";
    }
}
