package com.coderlee.designpattern.behavioral.strategy.advanced.dynamic;

/**
 * 具体策略：最短距离（公里数最少）
 *
 * @author coderlee
 */
public class ShortestDistanceStrategy implements RouteStrategy {

    @Override
    public RouteResult planRoute(String start, String end) {
        System.out.println("🚗 策略：最短距离（公里数最少）");
        // 模拟路线规划
        System.out.println("   → 分析路网结构...");
        System.out.println("   → 选择最短路径...");
        System.out.println("   → 计算距离...");

        // 模拟数据：短距离路线通常时间长（可能堵车）
        return new RouteResult("最短距离路线", 22.0, 45, 5.0);
    }

    @Override
    public String getStrategyName() {
        return "最短距离";
    }
}
