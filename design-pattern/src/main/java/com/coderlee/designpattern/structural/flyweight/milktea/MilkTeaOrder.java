package com.coderlee.designpattern.structural.flyweight.milktea;

/**
 * 奶茶订单
 * <p>
 * 处理顾客的奶茶订单
 * </p>
 *
 * @author coderlee
 */
public class MilkTeaOrder {

    private String orderNumber;

    public MilkTeaOrder(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * 点单
     * @param drinkType 奶茶类型
     * @param sugarLevel 糖度
     * @param iceLevel 冰度
     * @param cupSize 杯型
     * @param customerName 顾客姓名
     */
    public void placeOrder(String drinkType, String sugarLevel, String iceLevel,
                           String cupSize, String customerName) {
        System.out.println("\n订单号：" + orderNumber);
        TeaDrink drink = TeaDrinkFactory.getDrink(drinkType);
        drink.make(sugarLevel, iceLevel, cupSize, customerName);
    }
}
