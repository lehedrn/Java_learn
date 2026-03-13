package com.coderlee.designpattern.behavioral.strategy.advanced.dynamic;

/**
 * 上下文：导航系统
 * <p>
 * 支持在运行时动态切换路线策略
 * </p>
 *
 * @author coderlee
 */
public class NavigationSystem {

    /**
     * 当前使用的路线策略
     */
    private RouteStrategy currentStrategy;

    /**
     * 起点
     */
    private String currentStart;

    /**
     * 终点
     */
    private String currentEnd;

    /**
     * 设置路线策略
     * <p>
     * 可以在运行时动态切换
     * </p>
     *
     * @param strategy 路线策略
     */
    public void setRouteStrategy(RouteStrategy strategy) {
        this.currentStrategy = strategy;
        System.out.println("✅ 已切换策略：" + strategy.getStrategyName() + "\n");
    }

    /**
     * 设置目的地
     *
     * @param start 起点
     * @param end 终点
     */
    public void setDestination(String start, String end) {
        this.currentStart = start;
        this.currentEnd = end;
        System.out.println("📍 路线：" + start + " → " + end);
    }

    /**
     * 开始导航
     *
     * @return 路线结果
     */
    public RouteResult startNavigation() {
        if (currentStrategy == null) {
            System.out.println("❌ 请先选择路线偏好");
            return null;
        }

        if (currentStart == null || currentEnd == null) {
            System.out.println("❌ 请先设置目的地");
            return null;
        }

        System.out.println("🧭 开始规划路线...\n");
        return currentStrategy.planRoute(currentStart, currentEnd);
    }

    /**
     * 比较所有策略的路线结果
     */
    public void compareAllStrategies() {
        System.out.println("\n📊 对比所有路线方案：");
        System.out.println("路线：" + currentStart + " → " + currentEnd);
        System.out.println("═══════════════════════════════════════════════════\n");

        // 保存当前策略
        RouteStrategy originalStrategy = currentStrategy;

        // 测试所有策略
        RouteStrategy[] strategies = {
            new FastestRouteStrategy(),
            new ShortestDistanceStrategy(),
            new LeastCostStrategy()
        };

        for (RouteStrategy strategy : strategies) {
            System.out.println("--- 方案：" + strategy.getStrategyName() + " ---");
            RouteResult result = strategy.planRoute(currentStart, currentEnd);
            System.out.println("结果：" + result + "\n");
        }

        System.out.println("═══════════════════════════════════════════════════");

        // 恢复原策略
        currentStrategy = originalStrategy;
    }
}
