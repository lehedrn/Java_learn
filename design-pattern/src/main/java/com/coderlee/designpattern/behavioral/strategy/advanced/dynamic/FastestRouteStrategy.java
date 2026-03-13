package com.coderlee.designpattern.behavioral.strategy.advanced.dynamic;

/**
 * 具体策略：最快路线（高速优先）
 *
 * @author coderlee
 */
public class FastestRouteStrategy implements RouteStrategy {

    @Override
    public RouteResult planRoute(String start, String end) {
        System.out.println("🚗 策略：最快路线（高速优先）");
        // 模拟路线规划
        System.out.println("   → 分析实时路况...");
        System.out.println("   → 优先选择高速公路...");
        System.out.println("   → 计算预计时间...");

        // 模拟数据：高速路线通常距离较长但时间短
        return new RouteResult("高速优先路线", 35.5, 28, 15.0);
    }

    @Override
    public String getStrategyName() {
        return "最快路线";
    }
}
