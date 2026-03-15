package com.coderlee.designpattern.behavioral.chain.captive;

import java.math.BigDecimal;

/**
 * 捕获式职责链演示
 * 支付渠道选择系统
 */
public class CaptiveChainDemo {

    public static void main(String[] args) {
        System.out.println("========== 捕获式职责链演示：支付渠道选择 ==========\n");

        PaymentProcessorChain standardChain = PaymentProcessorChain.buildStandardChain();

        System.out.println("--- 场景 1: 用户首选支付宝 ---");
        PaymentContext ctx1 = new PaymentContext("PAY001", "U001", new BigDecimal("299.00"), PaymentChannel.ALIPAY);
        standardChain.process(ctx1);
        printResult(ctx1);

        System.out.println("\n--- 场景 2: 用户首选微信支付 ---");
        PaymentContext ctx2 = new PaymentContext("PAY002", "U002", new BigDecimal("599.00"), PaymentChannel.WECHAT_PAY);
        standardChain.process(ctx2);
        printResult(ctx2);

        System.out.println("\n--- 场景 3: 大额支付（用户无偏好）---");
        PaymentContext ctx3 = new PaymentContext("PAY003", "U003", new BigDecimal("15000.00"), null);
        standardChain.process(ctx3);
        printResult(ctx3);

        System.out.println("\n--- 场景 4: 小额支付（用户无偏好）---");
        PaymentContext ctx4 = new PaymentContext("PAY004", "U004", new BigDecimal("99.00"), null);
        standardChain.process(ctx4);
        printResult(ctx4);

        System.out.println("\n--- 场景 5: 超大额支付（超过支付宝限额）---");
        PaymentContext ctx5 = new PaymentContext("PAY005", "U005", new BigDecimal("60000.00"), PaymentChannel.ALIPAY);
        standardChain.process(ctx5);
        printResult(ctx5);

        System.out.println("\n--- 场景 6: 用户首选银联（大额）---");
        PaymentContext ctx6 = new PaymentContext("PAY006", "U006", new BigDecimal("25000.00"), PaymentChannel.UNION_PAY);
        standardChain.process(ctx6);
        printResult(ctx6);

        // 演示不同的链
        System.out.println("\n\n========== 使用快捷支付链 ==========\n");
        PaymentProcessorChain fastChain = PaymentProcessorChain.buildFastChain();

        PaymentContext ctx7 = new PaymentContext("FAST001", "U007", new BigDecimal("399.00"), PaymentChannel.WECHAT_PAY);
        fastChain.process(ctx7);
        printResult(ctx7);

        System.out.println("\n========== 使用大额支付链 ==========\n");
        PaymentProcessorChain highAmountChain = PaymentProcessorChain.buildHighAmountChain();

        PaymentContext ctx8 = new PaymentContext("VIP001", "U008", new BigDecimal("80000.00"), null);
        highAmountChain.process(ctx8);
        printResult(ctx8);

        // 对比两种模式的差异
        System.out.println("\n\n========== 两种职责链模式对比 ==========\n");
        System.out.println("【流水线模式】订单处理：");
        System.out.println("  验证 → 优惠券 → 库存 → 风控 → 仓库");
        System.out.println("  特点：每个节点都执行，全部通过才成功\n");

        System.out.println("【捕获式模式】支付选择：");
        System.out.println("  支付宝 → 微信支付 → 银联 → 余额 → 组合");
        System.out.println("  特点：第一个能处理的节点执行后，链条立即终止\n");
    }

    private static void printResult(PaymentContext context) {
        System.out.println("\n[支付结果]");
        System.out.println("订单号：" + context.getOrderId());
        System.out.println("金额：" + context.getAmount());
        System.out.println("结果：" + context.getResult());
    }
}
