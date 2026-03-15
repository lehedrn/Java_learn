package com.coderlee.designpattern.behavioral.chain;

import com.coderlee.designpattern.behavioral.chain.handler.*;

/**
 * 订单处理器链构建器
 * 负责组装责任链
 */
public class OrderProcessorChain {

    private final OrderHandler head;

    private OrderProcessorChain(OrderHandler head) {
        this.head = head;
    }

    /**
     * 构建标准订单处理链
     * 处理顺序：验证 → 优惠券 → 库存 → 风控 → 仓库分配
     */
    public static OrderProcessorChain buildStandardChain() {
        // 创建各个处理器
        OrderHandler validation = new OrderValidationHandler();
        OrderHandler coupon = new CouponHandler();
        OrderHandler inventory = new InventoryHandler();
        OrderHandler riskControl = new RiskControlHandler();
        OrderHandler warehouse = new WarehouseHandler();

        // 组装责任链
        validation.setNext(coupon)
                  .setNext(inventory)
                  .setNext(riskControl)
                  .setNext(warehouse);

        return new OrderProcessorChain(validation);
    }

    /**
     * 构建快速处理链（跳过优惠券和风控）
     * 处理顺序：验证 → 库存 → 仓库分配
     */
    public static OrderProcessorChain buildFastChain() {
        OrderHandler validation = new OrderValidationHandler();
        OrderHandler inventory = new InventoryHandler();
        OrderHandler warehouse = new WarehouseHandler();

        validation.setNext(inventory).setNext(warehouse);

        return new OrderProcessorChain(validation);
    }

    /**
     * 构建风控强化链（风控前置）
     * 处理顺序：验证 → 风控 → 优惠券 → 库存 → 仓库分配
     */
    public static OrderProcessorChain buildSecurityChain() {
        OrderHandler validation = new OrderValidationHandler();
        OrderHandler riskControl = new RiskControlHandler();
        OrderHandler coupon = new CouponHandler();
        OrderHandler inventory = new InventoryHandler();
        OrderHandler warehouse = new WarehouseHandler();

        validation.setNext(riskControl)
                  .setNext(coupon)
                  .setNext(inventory)
                  .setNext(warehouse);

        return new OrderProcessorChain(validation);
    }

    /**
     * 处理订单
     */
    public boolean process(Order order) {
        return head.handle(order);
    }
}
