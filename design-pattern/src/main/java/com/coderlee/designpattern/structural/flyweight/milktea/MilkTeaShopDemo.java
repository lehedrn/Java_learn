package com.coderlee.designpattern.structural.flyweight.milktea;

/**
 * 奶茶店演示类
 *
 * @author coderlee
 */
public class MilkTeaShopDemo {

    public static void main(String[] args) {
        System.out.println("========== 享元模式演示 - 奶茶店 ==========\n");

        MilkTeaOrder order1 = new MilkTeaOrder("20240312001");
        MilkTeaOrder order2 = new MilkTeaOrder("20240312002");
        MilkTeaOrder order3 = new MilkTeaOrder("20240312003");

        System.out.println("--- 早高峰订单 ---");

        // 订单 1：张三点了 2 杯珍珠奶茶
        order1.placeOrder("珍珠奶茶", "微糖", "少冰", "中杯", "张三");
        order1.placeOrder("珍珠奶茶", "正常糖", "去冰", "大杯", "张三");

        // 订单 2：李四点了一杯芝士奶盖和一杯水果茶
        order2.placeOrder("芝士奶盖茶", "半糖", "少冰", "中杯", "李四");
        order2.placeOrder("水果茶", "微糖", "正常冰", "大杯", "李四");

        // 订单 3：王五点了 3 杯，都是珍珠奶茶
        order3.placeOrder("珍珠奶茶", "无糖", "去冰", "中杯", "王五");
        order3.placeOrder("芝士奶盖茶", "正常糖", "正常冰", "大杯", "王五");
        order3.placeOrder("水果茶", "半糖", "少冰", "中杯", "王五");

        System.out.println("\n--- 统计 ---");
        System.out.println("总共制作了 7 杯奶茶");
        System.out.println("创建的奶茶类型对象数量：" + TeaDrinkFactory.getDrinkCount() + " 个");
        System.out.println("节省对象数量：" + (7 - TeaDrinkFactory.getDrinkCount()) + " 个");

        System.out.println("\n========== 演示结束 ==========");
    }
}
