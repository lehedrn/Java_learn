package com.coderlee.designpattern.structural.composite;

/**
 * 叶子节点：单个商品
 *
 * @author coderlee
 */
public class Product implements OrderItem {

    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public void show(String indent) {
        System.out.println(indent + "🛒 " + name + " × " + quantity
                + " = ￥" + String.format("%.2f", getPrice()));
    }

    @Override
    public double getPrice() {
        return price * quantity;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return name;
    }
}
