package com.coderlee.designpattern.behavioral.strategy.advanced.dynamic;

/**
 * 策略接口：路线规划策略
 *
 * @author coderlee
 */
public interface RouteStrategy {
    /**
     * 规划路线
     *
     * @param start 起点
     * @param end 终点
     * @return 路线信息
     */
    RouteResult planRoute(String start, String end);

    /**
     * 获取策略名称
     *
     * @return 策略名称
     */
    String getStrategyName();
}
