package com.coderlee.designpattern.behavioral.strategy.advanced.dynamic;

/**
 * 具体策略：最少费用（避免收费路段）
 *
 * @author coderlee
 */
public class LeastCostStrategy implements RouteStrategy {

    @Override
    public RouteResult planRoute(String start, String end) {
        System.out.println("🚗 策略：最少费用（避免收费路段）");
        // 模拟路线规划
        System.out.println("   → 分析收费路段...");
        System.out.println("   → 规划避费路线...");
        System.out.println("   → 计算总费用...");

        // 模拟数据：免费路线通常距离和时间都较长
        return new RouteResult("少收费路线", 30.0, 50, 2.0);
    }

    @Override
    public String getStrategyName() {
        return "最少费用";
    }
}
