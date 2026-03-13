package com.coderlee.designpattern.behavioral.strategy.advanced.factory;

/**
 * 具体策略：超级 VIP 折扣（85 折）
 *
 * @author coderlee
 */
public class SuperVipMemberDiscount implements DiscountStrategy {

    @Override
    public double calculateDiscountedPrice(double originalPrice) {
        // 超级 VIP 会员 85 折
        double discountedPrice = originalPrice * 0.85;
        System.out.printf("   超级 VIP 85 折：￥%.2f → ￥%.2f\n",
                originalPrice, discountedPrice);
        return discountedPrice;
    }

    @Override
    public String getDiscountName() {
        return "超级 VIP 85 折";
    }
}
