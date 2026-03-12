package com.coderlee.designpattern.structural.flyweight.milktea;

/**
 * 具体享元：芝士奶盖茶
 * <p>
 * 内在状态：奶茶类型、配方、基础价格
 * </p>
 *
 * @author coderlee
 */
public class CheeseMilkTea implements TeaDrink {

    private final String name = "芝士奶盖茶";
    private final String recipe = "绿茶底 + 芝士奶盖 + 鲜奶";
    private final double basePrice = 22.0;

    @Override
    public void make(String sugarLevel, String iceLevel, String cupSize, String customerName) {
        System.out.printf("🧀 %s 的 %s [￥%.2f]\n", customerName, name, getFinalPrice(cupSize));
        System.out.printf("   配方：%s\n", recipe);
        System.out.printf("   定制：%s 糖，%s, %s\n", sugarLevel, iceLevel, cupSize);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getBasePrice() {
        return basePrice;
    }

    private double getFinalPrice(String cupSize) {
        if ("大杯".equals(cupSize)) {
            return basePrice + 3;
        }
        return basePrice;
    }
}
