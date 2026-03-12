package com.coderlee.designpattern.structural.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 * <p>
 * 订单也是一种组合，可以包含多个商品或礼品盒
 * </p>
 *
 * @author coderlee
 */
public class Order implements OrderItem {

    private String orderId;
    private List<OrderItem> items;

    public Order(String orderId) {
        this.orderId = orderId;
        this.items = new ArrayList<>();
    }

    @Override
    public void show(String indent) {
        System.out.println(indent + "📋 订单号：" + orderId);
        for (OrderItem item : items) {
            item.show(indent + "    ");
        }
    }

    @Override
    public double getPrice() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getPrice();
        }
        return total;
    }

    @Override
    public int getQuantity() {
        int total = 0;
        for (OrderItem item : items) {
            total += item.getQuantity();
        }
        return total;
    }

    @Override
    public void add(OrderItem item) {
        items.add(item);
    }

    @Override
    public void remove(OrderItem item) {
        items.remove(item);
    }

    public int getItemCount() {
        return items.size();
    }

    @Override
    public String toString() {
        return "订单 " + orderId;
    }
}
