package com.coderlee.designpattern.structural.composite;

/**
 * 电商订单演示类
 *
 * @author coderlee
 */
public class OrderDemo {

    public static void main(String[] args) {
        System.out.println("========== 组合模式演示 - 电商订单 ==========\n");

        // 创建单个商品
        Product phone = new Product("iPhone 15", 5999.00, 1);
        Product case1 = new Product("手机壳", 29.99, 2);
        Product charger = new Product("充电器", 149.00, 1);

        // 创建耳机商品
        Product earbuds = new Product("AirPods", 1299.00, 1);

        // 创建礼品盒：苹果配件套装
        GiftBox appleGiftBox = new GiftBox("苹果配件套装");
        appleGiftBox.add(case1);
        appleGiftBox.add(charger);

        // 创建礼品盒：新年大礼包（可以嵌套礼品盒）
        GiftBox newYearBox = new GiftBox("新年大礼包");
        newYearBox.add(earbuds);
        newYearBox.add(appleGiftBox);  // 嵌套礼品盒

        // 创建订单
        Order order = new Order("20240115001");
        order.add(phone);
        order.add(newYearBox);

        System.out.println("--- 订单详情 ---");
        order.show("");

        System.out.println("\n--- 订单统计 ---");
        System.out.println("商品总数：" + order.getQuantity());
        System.out.println("订单总价：￥" + String.format("%.2f", order.getPrice()));

        System.out.println("\n--- 移除商品 ---");
        order.remove(newYearBox);
        System.out.println("移除后的订单：");
        order.show("");
        System.out.println("订单总价：￥" + String.format("%.2f", order.getPrice()));

        System.out.println("\n========== 演示结束 ==========");
    }
}
