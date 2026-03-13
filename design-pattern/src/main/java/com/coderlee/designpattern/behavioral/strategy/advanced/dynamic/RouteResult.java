package com.coderlee.designpattern.behavioral.strategy.advanced.dynamic;

/**
 * 路线结果类
 *
 * @author coderlee
 */
public class RouteResult {
    /**
     * 路线名称
     */
    private final String routeName;

    /**
     * 距离（km）
     */
    private final double distance;

    /**
     * 预计时间（分钟）
     */
    private final int estimatedTime;

    /**
     * 费用（元）
     */
    private final double cost;

    public RouteResult(String routeName, double distance, int estimatedTime, double cost) {
        this.routeName = routeName;
        this.distance = distance;
        this.estimatedTime = estimatedTime;
        this.cost = cost;
    }

    public String getRouteName() {
        return routeName;
    }

    public double getDistance() {
        return distance;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return String.format("%s | 距离：%.1fkm | 时间：%d分钟 | 费用：￥%.1f",
                routeName, distance, estimatedTime, cost);
    }
}
