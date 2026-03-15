package com.coderlee.designpattern.behavioral.chain.handler;

import com.coderlee.designpattern.behavioral.chain.Order;
import com.coderlee.designpattern.behavioral.chain.OrderHandler;

import java.math.BigDecimal;

/**
 * 风险控制处理器
 * 职责：根据订单金额进行风控审核
 */
public class RiskControlHandler extends OrderHandler {

    private static final BigDecimal HIGH_VALUE_THRESHOLD = new BigDecimal("5000");
    private static final BigDecimal SUPER_HIGH_VALUE_THRESHOLD = new BigDecimal("20000");

    @Override
    protected boolean doHandle(Order order) {
        BigDecimal amount = order.getAmount();

        // 超高价值订单需要人工审核（这里模拟审核通过）
        if (amount.compareTo(SUPER_HIGH_VALUE_THRESHOLD) >= 0) {
            log("超高价值订单（>20000），转交人工审核");
            order.addLog("风控检查：超高价值订单，已转人工审核");
            return true;
        }

        // 高价值订单需要额外验证
        if (amount.compareTo(HIGH_VALUE_THRESHOLD) >= 0) {
            log("高价值订单（>5000），进行额外验证");
            order.addLog("风控检查：高价值订单，额外验证通过");
            return true;
        }

        // 普通金额订单，风控通过
        log("✓ 订单金额正常，风控通过");
        order.addLog("风控检查：通过");
        return true;
    }
}
