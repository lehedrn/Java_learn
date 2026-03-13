package com.coderlee.designpattern.behavioral.strategy.advanced.factory;

/**
 * 具体策略：VIP 会员折扣（9 折）
 *
 * @author coderlee
 */
public class VipMemberDiscount implements DiscountStrategy {

    @Override
    public double calculateDiscountedPrice(double originalPrice) {
        // VIP 会员 9 折
        double discountedPrice = originalPrice * 0.90;
        System.out.printf("   VIP 会员 9 折：￥%.2f → ￥%.2f\n",
                originalPrice, discountedPrice);
        return discountedPrice;
    }

    @Override
    public String getDiscountName() {
        return "VIP 会员 9 折";
    }
}
