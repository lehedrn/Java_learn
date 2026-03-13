package com.coderlee.designpattern.behavioral.strategy.basic;

/**
 * 策略模式基础用法演示 - 支付方式
 * <p>
 * 演示场景：
 * 1. 用户购物车结算
 * 2. 可以选择不同的支付方式（支付宝/微信/银行卡）
 * 3. 不同支付方式可以互相替换，无需修改购物车代码
 * </p>
 *
 * @author coderlee
 */
public class BasicStrategyDemo {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║     策略模式 - 基础用法                           ║");
        System.out.println("║     场景：电商购物车支付                         ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        // 创建购物车（上下文）
        ShoppingCart cart = new ShoppingCart();

        // 添加商品
        cart.addItem("iPhone 15", 5999.00);
        cart.addItem("AirPods", 1299.00);
        cart.addItem("手机壳", 49.00);

        System.out.println("\n--- 第一次购物：使用支付宝支付 ---\n");
        // 设置支付策略：支付宝
        PaymentStrategy alipay = new AlipayStrategy("zhangsan@alipay.com");
        cart.setPaymentStrategy(alipay);
        cart.checkout();

        // 重新购物，使用微信支付
        System.out.println("\n\n--- 第二次购物：使用微信支付 ---\n");
        ShoppingCart cart2 = new ShoppingCart();
        cart2.addItem("小米电视", 2999.00);
        cart2.addItem("小米手环", 299.00);

        PaymentStrategy wechat = new WechatPayStrategy("wx_openid_123456");
        cart2.setPaymentStrategy(wechat);
        cart2.checkout();

        // 重新购物，使用银行卡支付
        System.out.println("\n\n--- 第三次购物：使用银行卡支付 ---\n");
        ShoppingCart cart3 = new ShoppingCart();
        cart3.addItem("MacBook Pro", 12999.00);

        PaymentStrategy bankCard = new BankCardStrategy("招商银行", "6225888888888888");
        cart3.setPaymentStrategy(bankCard);
        cart3.checkout();

        System.out.println("\n========== 演示结束 ==========");
    }
}
