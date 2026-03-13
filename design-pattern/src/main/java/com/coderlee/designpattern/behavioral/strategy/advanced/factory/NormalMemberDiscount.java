package com.coderlee.designpattern.behavioral.strategy.advanced.factory;

/**
 * 具体策略：普通会员折扣（95 折）
 *
 * @author coderlee
 */
public class NormalMemberDiscount implements DiscountStrategy {

    @Override
    public double calculateDiscountedPrice(double originalPrice) {
        // 普通会员 95 折
        double discountedPrice = originalPrice * 0.95;
        System.out.printf("   普通会员 95 折：￥%.2f → ￥%.2f\n",
                originalPrice, discountedPrice);
        return discountedPrice;
    }

    @Override
    public String getDiscountName() {
        return "普通会员 95 折";
    }
}
