package com.coderlee.designpattern.behavioral.strategy.basic;

/**
 * 上下文：购物车
 * <p>
 * 持有支付策略的引用，客户端决定使用哪种支付策略
 * </p>
 *
 * @author coderlee
 */
public class ShoppingCart {

    /**
     * 当前使用的支付策略
     */
    private PaymentStrategy paymentStrategy;

    /**
     * 购物车中的商品总金额
     */
    private double totalAmount;

    /**
     * 设置支付策略
     * <p>
     * 客户端可以在运行时动态切换支付策略
     * </p>
     *
     * @param paymentStrategy 支付策略
     */
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
        System.out.println("✅ 已选择支付方式：" + paymentStrategy.getPaymentMethod());
    }

    /**
     * 添加商品到购物车
     *
     * @param itemName 商品名称
     * @param price 商品价格
     */
    public void addItem(String itemName, double price) {
        System.out.println("🛒 添加商品：" + itemName + " ￥" + price);
        totalAmount += price;
    }

    /**
     *  checkout：使用当前策略支付
     *
     * @return 是否支付成功
     */
    public boolean checkout() {
        if (paymentStrategy == null) {
            System.out.println("❌ 请先选择支付方式");
            return false;
        }

        System.out.println("\n===== 订单结算 =====");
        System.out.println("订单总金额：￥" + totalAmount);
        System.out.println("==================\n");

        // 委托给策略执行支付
        return paymentStrategy.pay(totalAmount);
    }

    /**
     * 获取订单总金额
     *
     * @return 总金额
     */
    public double getTotalAmount() {
        return totalAmount;
    }
}
