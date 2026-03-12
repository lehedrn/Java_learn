package com.coderlee.designpattern.structural.decorator;

/**
 * 装饰器模式演示类
 * <p>
 * 通过咖啡订单系统展示装饰器模式的使用
 * 演示如何动态地为咖啡添加各种配料
 * </p>
 *
 * @author coderlee
 */
public class DecoratorDemo {

    public static void main(String[] args) {
        System.out.println("========== 装饰器模式演示 - 咖啡订单系统 ==========\n");

        // 订单 1: 基础咖啡
        System.out.println("--- 订单 1: 基础咖啡 ---");
        Coffee order1 = new SimpleCoffee();
        printOrder(order1);

        // 订单 2: 基础咖啡 + 牛奶
        System.out.println("\n--- 订单 2: 咖啡 + 牛奶 ---");
        Coffee order2 = new MilkDecorator(new SimpleCoffee());
        printOrder(order2);

        // 订单 3: 基础咖啡 + 牛奶 + 糖
        System.out.println("\n--- 订单 3: 咖啡 + 牛奶 + 糖 ---");
        Coffee order3 = new SugarDecorator(new MilkDecorator(new SimpleCoffee()));
        printOrder(order3);

        // 订单 4: 基础咖啡 + 摩卡 + 奶油 + 牛奶（豪华咖啡）
        System.out.println("\n--- 订单 4: 豪华咖啡（摩卡 + 奶油 + 牛奶） ---");
        Coffee order4 = new WhippingDecorator(
                new MilkDecorator(
                        new MochaDecorator(new SimpleCoffee())
                )
        );
        printOrder(order4);

        // 订单 5: 使用变量逐步构建（更灵活的写法）
        System.out.println("\n--- 订单 5: 逐步构建的定制咖啡 ---");
        Coffee coffee = new SimpleCoffee();
        System.out.println("初始：" + coffee.getDescription() + " = ￥" + coffee.getCost());

        coffee = new MochaDecorator(coffee);
        System.out.println("添加摩卡后：" + coffee.getDescription() + " = ￥" + coffee.getCost());

        coffee = new SugarDecorator(coffee);
        System.out.println("添加糖后：" + coffee.getDescription() + " = ￥" + coffee.getCost());

        System.out.println("\n========== 演示结束 ==========");
    }

    /**
     * 打印订单信息
     * @param coffee 咖啡对象
     */
    private static void printOrder(Coffee coffee) {
        System.out.println("描述：" + coffee.getDescription());
        System.out.printf("价格：￥%.2f\n", coffee.getCost());
    }
}
