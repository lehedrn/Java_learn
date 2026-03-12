package com.coderlee.designpattern.behavioral.template.hook;

/**
 * 带钩子的模板方法演示 - 外卖订单处理
 * <p>
 * 演示场景：
 * 1. 餐厅订单处理系统
 * 2. 不同类型的订单（堂食、外卖、自取）有不同的处理流程
 * 3. 使用钩子方法控制是否需要配送和发票
 * </p>
 *
 * @author coderlee
 */
public class HookTemplateDemo {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║     模板方法模式 - 带钩子的模板方法               ║");
        System.out.println("║     场景：餐厅订单处理系统                       ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        // 订单 1：堂食订单（不需要配送，不需要发票）
        System.out.println("\n【订单 1】堂食订单 - 宫保鸡丁饭");
        OrderProcessor dineInOrder = new DineInOrderProcessor();
        dineInOrder.processOrder();

        // 订单 2：外卖订单（需要配送，需要发票）
        System.out.println("\n【订单 2】外卖订单 - 鱼香肉丝饭");
        OrderProcessor deliveryOrder = new DeliveryOrderProcessor(
                "北京市朝阳区 xx 路 xx 号", true);
        deliveryOrder.processOrder();

        // 订单 3：自取订单（不需要配送，需要发票）
        System.out.println("\n【订单 3】自取订单 - 红烧牛肉面");
        OrderProcessor pickupOrder = new PickupOrderProcessor(true);
        pickupOrder.processOrder();

        System.out.println("========== 演示结束 ==========");
    }
}
