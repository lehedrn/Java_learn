package com.coderlee.designpattern.structural.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合节点：礼品盒/商品组合
 * <p>
 * 可以包含多个商品或其他礼品盒
 * </p>
 *
 * @author coderlee
 */
public class GiftBox implements OrderItem {

    private String name;
    private List<OrderItem> items;

    public GiftBox(String name) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    @Override
    public void show(String indent) {
        System.out.println(indent + "🎁 " + name + " [礼品盒]");
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
        System.out.println("添加 " + item + " 到 " + name);
    }

    @Override
    public void remove(OrderItem item) {
        items.remove(item);
        System.out.println("从 " + name + " 移除 " + item);
    }

    public int getItemCount() {
        return items.size();
    }

    @Override
    public String toString() {
        return name + " (礼品盒)";
    }
}
