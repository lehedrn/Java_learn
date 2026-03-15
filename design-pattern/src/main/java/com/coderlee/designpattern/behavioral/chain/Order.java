package com.coderlee.designpattern.behavioral.chain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单实体类
 */
public class Order {

    private final String orderId;
    private final BigDecimal amount;
    private final OrderType orderType;
    private final List<String> processLogs;

    public Order(String orderId, BigDecimal amount, OrderType orderType) {
        this.orderId = orderId;
        this.amount = amount;
        this.orderType = orderType;
        this.processLogs = new ArrayList<>();
    }

    public String getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void addLog(String log) {
        processLogs.add(LocalDateTime.now() + " - " + log);
    }

    public List<String> getProcessLogs() {
        return processLogs;
    }

    @Override
    public String toString() {
        return "Order{orderId='" + orderId + "', amount=" + amount + ", type=" + orderType + "}";
    }
}
