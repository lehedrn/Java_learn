package com.coderlee.designpattern.structural.decorator;

/**
 * 客户端测试类
 * <p>
 * 测试装饰器模式的各种场景
 * </p>
 *
 * @author coderlee
 */
public class Client {

    public static void main(String[] args) {
        // 测试 1: 验证基础咖啡
        testSimpleCoffee();

        // 测试 2: 验证单个装饰器
        testSingleDecorator();

        // 测试 3: 验证多个装饰器嵌套
        testMultipleDecorators();

        // 测试 4: 验证装饰器的灵活性
        testDecoratorFlexibility();
    }

    /**
     * 测试基础咖啡
     */
    private static void testSimpleCoffee() {
        System.out.println("【测试 1: 基础咖啡】");
        Coffee coffee = new SimpleCoffee();
        assert coffee.getDescription().equals("基础咖啡") : "描述应该是'基础咖啡'";
        assert coffee.getCost() == 5.0 : "价格应该是 5.0";
        System.out.println("✓ 基础咖啡测试通过\n");
    }

    /**
     * 测试单个装饰器
     */
    private static void testSingleDecorator() {
        System.out.println("【测试 2: 单个装饰器】");
        Coffee milkCoffee = new MilkDecorator(new SimpleCoffee());
        System.out.println("牛奶咖啡：" + milkCoffee.getDescription() + " = ￥" + milkCoffee.getCost());
        assert milkCoffee.getDescription().contains("牛奶") : "描述应该包含'牛奶'";
        assert milkCoffee.getCost() == 7.0 : "价格应该是 7.0";
        System.out.println("✓ 单个装饰器测试通过\n");
    }

    /**
     * 测试多个装饰器嵌套
     */
    private static void testMultipleDecorators() {
        System.out.println("【测试 3: 多个装饰器嵌套】");
        Coffee customCoffee = new WhippingDecorator(
                new SugarDecorator(
                        new MochaDecorator(
                                new SimpleCoffee()
                        )
                )
        );
        System.out.println("定制咖啡：" + customCoffee.getDescription() + " = ￥" + customCoffee.getCost());
        // 基础 5.0 + 摩卡 3.0 + 糖 0.5 + 奶油 2.5 = 11.0
        assert customCoffee.getCost() == 11.0 : "价格应该是 11.0";
        System.out.println("✓ 多个装饰器嵌套测试通过\n");
    }

    /**
     * 测试装饰器的灵活性 - 同一组件可以有不同的装饰组合
     */
    private static void testDecoratorFlexibility() {
        System.out.println("【测试 4: 装饰器的灵活性】");

        // 创建基础组件
        Coffee baseCoffee = new SimpleCoffee();

        // 不同的装饰组合
        Coffee coffee1 = new MilkDecorator(baseCoffee);
        Coffee coffee2 = new MochaDecorator(baseCoffee);
        Coffee coffee3 = new SugarDecorator(new MilkDecorator(baseCoffee));

        System.out.println("咖啡 1: " + coffee1.getDescription() + " = ￥" + coffee1.getCost());
        System.out.println("咖啡 2: " + coffee2.getDescription() + " = ￥" + coffee2.getCost());
        System.out.println("咖啡 3: " + coffee3.getDescription() + " = ￥" + coffee3.getCost());

        // 验证互不影响
        assert coffee1.getCost() == 7.0 : "咖啡 1 价格应该是 7.0";
        assert coffee2.getCost() == 8.0 : "咖啡 2 价格应该是 8.0";
        assert coffee3.getCost() == 7.5 : "咖啡 3 价格应该是 7.5";

        System.out.println("✓ 装饰器灵活性测试通过\n");
    }
}
