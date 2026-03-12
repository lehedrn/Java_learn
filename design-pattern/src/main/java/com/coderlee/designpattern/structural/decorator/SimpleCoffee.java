package com.coderlee.designpattern.structural.decorator;

/**
 * 基础咖啡（具体组件）
 * <p>
 * 实现咖啡组件接口，提供最基本的咖啡
 * </p>
 *
 * @author coderlee
 */
public class SimpleCoffee extends Coffee {

    public SimpleCoffee() {
        this.description = "基础咖啡";
    }

    @Override
    public double getCost() {
        return 5.0;
    }
}
