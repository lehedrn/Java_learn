package com.coderlee.designpattern.behavioral.strategy.advanced.factory;

/**
 * 策略 + 工厂模式演示 - 会员折扣系统
 * <p>
 * 演示场景：
 * 1. 电商平台有不同会员等级
 * 2. 根据会员等级自动应用对应折扣
 * 3. 使用工厂模式统一管理策略创建
 * </p>
 *
 * @author coderlee
 */
public class FactoryStrategyDemo {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║     策略模式 - 进阶用法 1：策略 + 工厂             ║");
        System.out.println("║     场景：电商会员折扣系统                       ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        // 创建不同会员的订单
        System.out.println("--- 订单 1：普通用户（无会员） ---");
        OrderService order1 = new OrderService("ORD-001", 1000.00);
        order1.setDiscountByLevel(null);  // 无会员等级
        order1.calculateFinalPrice();

        System.out.println("--- 订单 2：普通会员 ---");
        OrderService order2 = new OrderService("ORD-002", 1000.00);
        order2.setDiscountByLevel(DiscountStrategyFactory.MemberLevel.NORMAL);
        order2.calculateFinalPrice();

        System.out.println("--- 订单 3：VIP 会员 ---");
        OrderService order3 = new OrderService("ORD-003", 1000.00);
        order3.setDiscountByLevel(DiscountStrategyFactory.MemberLevel.VIP);
        order3.calculateFinalPrice();

        System.out.println("--- 订单 4：超级 VIP ---");
        OrderService order4 = new OrderService("ORD-004", 1000.00);
        order4.setDiscountByLevel(DiscountStrategyFactory.MemberLevel.SUPER_VIP);
        order4.calculateFinalPrice();

        // 演示工厂模式的优势：客户端不需要知道具体策略类
        System.out.println("--- 订单 5：通过工厂获取策略 ---");
        OrderService order5 = new OrderService("ORD-005", 5000.00);
        // 客户端只需要知道会员等级，不需要 new 具体策略类
        order5.setDiscountByLevel(DiscountStrategyFactory.MemberLevel.VIP);
        order5.calculateFinalPrice();

        System.out.println("========== 演示结束 ==========");
    }
}
