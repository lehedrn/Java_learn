package com.coderlee.designpattern.behavioral.strategy.advanced.enumstrategy;

/**
 * 上下文：运费计算器
 *
 * @author coderlee
 */
public class ShippingCalculator {

    /**
     * 当前使用的运费策略
     */
    private ShippingStrategy strategy;

    /**
     * 设置快递公司（策略）
     *
     * @param strategy 运费策略
     */
    public void setShippingStrategy(ShippingStrategy strategy) {
        this.strategy = strategy;
        System.out.println("✅ 已选择快递：" + strategy.getCourierName());
    }

    /**
     * 计算运费
     *
     * @param packageName 包裹名称
     * @param weight 重量（kg）
     * @param distance 距离（km）
     * @return 运费
     */
    public double calculateShipping(String packageName, double weight, double distance) {
        if (strategy == null) {
            System.out.println("❌ 请先选择快递公司");
            return 0;
        }

        System.out.println("\n📦 包裹：" + packageName);
        System.out.println("   重量：" + weight + "kg");
        System.out.println("   距离：" + distance + "km");

        double fee = strategy.calculateShippingFee(weight, distance);
        int days = strategy.estimateDeliveryDays(distance);
        System.out.println("   预计送达：" + days + " 天");

        return fee;
    }

    /**
     * 比较不同快递的价格
     *
     * @param weight 重量
     * @param distance 距离
     */
    public void compareCouriers(double weight, double distance) {
        System.out.println("\n📊 不同快递价格对比：");
        System.out.println("   重量：" + weight + "kg, 距离：" + distance + "km");
        System.out.println("   ────────────────────────────────");

        for (CourierStrategy courier : CourierStrategy.values()) {
            double fee = courier.calculateShippingFee(weight, distance);
            int days = courier.estimateDeliveryDays(distance);
            System.out.printf("   %-10s: ￥%-6.2f  预计%d天\n",
                    courier.getCourierName(), fee, days);
        }
        System.out.println("   ────────────────────────────────");
    }
}
