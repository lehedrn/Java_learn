package com.coderlee.designpattern.structural.flyweight.milktea;

/**
 * 享元接口：奶茶类型
 *
 * @author coderlee
 */
public interface TeaDrink {
    /**
     * 制作奶茶
     * @param sugarLevel 糖度
     * @param iceLevel 冰度
     * @param cupSize 杯型
     * @param customerName 顾客姓名
     */
    void make(String sugarLevel, String iceLevel, String cupSize, String customerName);

    /**
     * 获取奶茶名称
     * @return 名称
     */
    String getName();

    /**
     * 获取基础价格
     * @return 价格
     */
    double getBasePrice();
}
