package com.coderlee.designpattern.behavioral.chain.handler;

import com.coderlee.designpattern.behavioral.chain.Order;
import com.coderlee.designpattern.behavioral.chain.OrderHandler;
import com.coderlee.designpattern.behavioral.chain.OrderType;

/**
 * 仓库分配处理器
 * 职责：根据订单类型分配发货仓库
 * 这是链条的最后一个节点
 */
public class WarehouseHandler extends OrderHandler {

    @Override
    protected boolean doHandle(Order order) {
        OrderType type = order.getOrderType();

        switch (type) {
            case OVERSEAS:
                log("分配保税仓发货");
                order.addLog("仓库分配：保税仓");
                return true;

            case GROUP_BUY:
                log("分配中心仓发货（团购订单）");
                order.addLog("仓库分配：中心仓");
                return true;

            case FLASH_SALE:
                log("分配前置仓发货（秒杀订单，追求速度）");
                order.addLog("仓库分配：前置仓");
                return true;

            default:
                log("✓ 分配普通仓库发货");
                order.addLog("仓库分配：普通仓库");
                return true;
        }
    }
}
