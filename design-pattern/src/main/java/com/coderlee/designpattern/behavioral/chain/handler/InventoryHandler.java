package com.coderlee.designpattern.behavioral.chain.handler;

import com.coderlee.designpattern.behavioral.chain.Order;
import com.coderlee.designpattern.behavioral.chain.OrderHandler;
import com.coderlee.designpattern.behavioral.chain.OrderType;

/**
 * 库存处理器
 * 职责：检查并锁定库存
 */
public class InventoryHandler extends OrderHandler {

    // 模拟库存检查
    private boolean hasStock(Order order) {
        // 秒杀订单需要特别检查
        if (order.getOrderType() == OrderType.FLASH_SALE) {
            // 模拟秒杀库存紧张
            System.out.println("  [库存检查] 秒杀商品库存紧张，剩余：10 件");
            return true;
        }

        // 海外购订单需要检查保税仓库存
        if (order.getOrderType() == OrderType.OVERSEAS) {
            System.out.println("  [库存检查] 检查保税仓库存...");
            return true;
        }

        // 普通订单默认有库存
        return true;
    }

    @Override
    protected boolean doHandle(Order order) {
        log("开始检查库存...");

        if (!hasStock(order)) {
            log("库存不足，无法处理");
            order.addLog("库存检查：失败");
            return false;
        }

        log("✓ 库存充足，已锁定库存");
        order.addLog("库存已锁定");
        return true;
    }
}
