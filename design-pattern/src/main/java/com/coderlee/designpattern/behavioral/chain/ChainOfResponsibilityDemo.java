package com.coderlee.designpattern.behavioral.chain;

import java.math.BigDecimal;

/**
 * 职责链模式演示类
 * 电商订单处理系统
 */
public class ChainOfResponsibilityDemo {

    public static void main(String[] args) {
        System.out.println("========== 职责链模式演示：电商订单处理系统 ==========\n");

        // 使用标准处理链
        OrderProcessorChain standardChain = OrderProcessorChain.buildStandardChain();

        System.out.println("--- 场景 1: 普通订单 ---");
        Order normalOrder = new Order("ORD001", new BigDecimal("299.00"), OrderType.NORMAL);
        standardChain.process(normalOrder);
        printOrderResult(normalOrder);

        System.out.println("\n--- 场景 2: 团购订单（满 500）---");
        Order groupOrder = new Order("ORD002", new BigDecimal("699.00"), OrderType.GROUP_BUY);
        standardChain.process(groupOrder);
        printOrderResult(groupOrder);

        System.out.println("\n--- 场景 3: 秒杀订单 ---");
        Order flashOrder = new Order("ORD003", new BigDecimal("99.00"), OrderType.FLASH_SALE);
        standardChain.process(flashOrder);
        printOrderResult(flashOrder);

        System.out.println("\n--- 场景 4: 高价值订单（>5000）---");
        Order highValueOrder = new Order("ORD004", new BigDecimal("8888.00"), OrderType.NORMAL);
        standardChain.process(highValueOrder);
        printOrderResult(highValueOrder);

        System.out.println("\n--- 场景 5: 海外购订单 ---");
        Order overseasOrder = new Order("ORD005", new BigDecimal("1599.00"), OrderType.OVERSEAS);
        standardChain.process(overseasOrder);
        printOrderResult(overseasOrder);

        System.out.println("\n--- 场景 6: 预售订单（满 1000）---");
        Order preSaleOrder = new Order("ORD006", new BigDecimal("1299.00"), OrderType.PRE_SALE);
        standardChain.process(preSaleOrder);
        printOrderResult(preSaleOrder);

        // 演示不同的处理链
        System.out.println("\n\n========== 使用快速处理链（秒杀场景）==========\n");
        OrderProcessorChain fastChain = OrderProcessorChain.buildFastChain();

        Order flashOrder2 = new Order("FLASH001", new BigDecimal("199.00"), OrderType.FLASH_SALE);
        fastChain.process(flashOrder2);
        printOrderResult(flashOrder2);

        System.out.println("\n========== 使用风控强化链（高价值场景）==========\n");
        OrderProcessorChain securityChain = OrderProcessorChain.buildSecurityChain();

        Order superHighOrder = new Order("VIP001", new BigDecimal("25000.00"), OrderType.NORMAL);
        securityChain.process(superHighOrder);
        printOrderResult(superHighOrder);
    }

    private static void printOrderResult(Order order) {
        System.out.println("\n[订单处理结果]");
        System.out.println("订单号：" + order.getOrderId());
        System.out.println("金额：" + order.getAmount());
        System.out.println("类型：" + order.getOrderType());
        System.out.println("处理日志:");
        for (String log : order.getProcessLogs()) {
            System.out.println("  → " + log);
        }
    }
}
