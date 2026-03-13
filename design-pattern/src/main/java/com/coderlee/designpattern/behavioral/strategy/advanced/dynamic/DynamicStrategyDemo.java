package com.coderlee.designpattern.behavioral.strategy.advanced.dynamic;

/**
 * 策略模式 - 动态切换策略演示
 * <p>
 * 演示场景：导航系统 - 同一路线，不同偏好
 * </p>
 *
 * @author coderlee
 */
public class DynamicStrategyDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║      策略模式 - 动态切换策略演示             ║");
        System.out.println("║        导航系统：同一路线，不同偏好          ║");
        System.out.println("╚════════════════════════════════════════════╝\n");

        // 创建导航系统
        NavigationSystem navigation = new NavigationSystem();

        // 设置起点和终点
        navigation.setDestination("北京市海淀区中关村", "北京市朝阳区国贸三期");

        System.out.println("═══════════════════════════════════════════════\n");

        // -----------------------------------------------------------------
        // 场景 1：赶时间 - 使用最快路线策略
        // -----------------------------------------------------------------
        System.out.println("【场景 1】赶时间开会 - 选择【最快路线】\n");
        navigation.setRouteStrategy(new FastestRouteStrategy());
        RouteResult result1 = navigation.startNavigation();
        if (result1 != null) {
            System.out.println("导航结果：" + result1);
        }
        System.out.println();

        Thread.sleep(500); // 模拟思考时间

        // -----------------------------------------------------------------
        // 场景 2：省油钱 - 使用最少费用策略
        // -----------------------------------------------------------------
        System.out.println("【场景 2】想省油钱 - 选择【最少费用】\n");
        navigation.setRouteStrategy(new LeastCostStrategy());
        RouteResult result2 = navigation.startNavigation();
        if (result2 != null) {
            System.out.println("导航结果：" + result2);
        }
        System.out.println();

        Thread.sleep(500);

        // -----------------------------------------------------------------
        // 场景 3：电动车 - 使用最短距离策略
        // -----------------------------------------------------------------
        System.out.println("【场景 3】开电动车 - 选择【最短距离】\n");
        navigation.setRouteStrategy(new ShortestDistanceStrategy());
        RouteResult result3 = navigation.startNavigation();
        if (result3 != null) {
            System.out.println("导航结果：" + result3);
        }
        System.out.println();

        Thread.sleep(500);

        // -----------------------------------------------------------------
        // 场景 4：对比所有策略，帮助决策
        // -----------------------------------------------------------------
        System.out.println("【场景 4】选择困难症 - 对比所有策略\n");
        navigation.compareAllStrategies();

        System.out.println("\n═══════════════════════════════════════════════");
        System.out.println("演示完成！");
        System.out.println("═══════════════════════════════════════════════");
    }
}
