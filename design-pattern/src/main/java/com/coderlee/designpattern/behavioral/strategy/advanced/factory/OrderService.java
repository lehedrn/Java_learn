package com.coderlee.designpattern.behavioral.strategy.advanced.factory;

/**
 * 上下文：订单服务
 * <p>
 * 使用折扣策略计算订单价格
 * </p>
 *
 * @author coderlee
 */
public class OrderService {

    /**
     * 订单 ID
     */
    private final String orderId;

    /**
     * 订单原价
     */
    private final double originalPrice;

    /**
     * 当前使用的折扣策略
     */
    private DiscountStrategy discountStrategy;

    public OrderService(String orderId, double originalPrice) {
        this.orderId = orderId;
        this.originalPrice = originalPrice;
    }

    /**
     * 设置折扣策略（通过工厂获取）
     *
     * @param level 会员等级
     */
    public void setDiscountByLevel(DiscountStrategyFactory.MemberLevel level) {
        this.discountStrategy = DiscountStrategyFactory.getStrategy(level);
        System.out.println("✅ 已应用折扣：" + discountStrategy.getDiscountName());
    }

    /**
     * 直接设置折扣策略
     *
     * @param strategy 折扣策略
     */
    public void setDiscountStrategy(DiscountStrategy strategy) {
        this.discountStrategy = strategy;
    }

    /**
     * 计算最终价格
     *
     * @return 最终价格
     */
    public double calculateFinalPrice() {
        System.out.println("\n===== 订单 " + orderId + " =====");
        System.out.println("原价：￥" + originalPrice);

        if (discountStrategy == null) {
            // 默认无折扣
            discountStrategy = new NoDiscount();
        }

        double finalPrice = discountStrategy.calculateDiscountedPrice(originalPrice);
        System.out.println("最终价格：￥" + finalPrice);
        System.out.println("=========================\n");

        return finalPrice;
    }

    /**
     * 获取订单 ID
     *
     * @return 订单 ID
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 获取原价
     *
     * @return 原价
     */
    public double getOriginalPrice() {
        return originalPrice;
    }
}
