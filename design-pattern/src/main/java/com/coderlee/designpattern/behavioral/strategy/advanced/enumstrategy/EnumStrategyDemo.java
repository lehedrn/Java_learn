package com.coderlee.designpattern.behavioral.strategy.advanced.enumstrategy;

/**
 * 策略 + 枚举模式演示 - 快递运费计算
 * <p>
 * 演示场景：
 * 1. 电商平台支持多家快递公司
 * 2. 每家公司有不同的运费计算规则
 * 3. 使用枚举实现策略，代码更简洁
 * </p>
 *
 * @author coderlee
 */
public class EnumStrategyDemo {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║     策略模式 - 进阶用法 2：策略 + 枚举             ║");
        System.out.println("║     场景：快递运费计算                           ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        ShippingCalculator calculator = new ShippingCalculator();

        // 对比所有快递的价格
        System.out.println("===== 对比所有快递公司 =====");
        calculator.compareCouriers(5.0, 600.0);

        // 选择顺丰
        System.out.println("\n===== 订单 1：选择顺丰速运 =====");
        calculator.setShippingStrategy(CourierStrategy.SF_EXPRESS);
        calculator.calculateShipping("iPhone 15", 0.5, 600.0);

        // 选择中通
        System.out.println("\n===== 订单 2：选择中通快递 =====");
        calculator.setShippingStrategy(CourierStrategy.ZTO_EXPRESS);
        calculator.calculateShipping("小米电视", 15.0, 1200.0);

        // 选择邮政
        System.out.println("\n===== 订单 3：选择邮政平邮 =====");
        calculator.setShippingStrategy(CourierStrategy.CHINA_POST);
        calculator.calculateShipping("旧衣服捐赠", 10.0, 800.0);

        // 选择京东
        System.out.println("\n===== 订单 4：选择京东物流 =====");
        calculator.setShippingStrategy(CourierStrategy.JD_LOGISTICS);
        calculator.calculateShipping("京东自营商品", 3.0, 300.0);

        System.out.println("\n========== 演示结束 ==========");
    }
}
