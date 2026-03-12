package com.coderlee.designpattern.structural.decorator;

/**
 * 糖装饰器（具体装饰器）
 * <p>
 * 为咖啡添加糖配料
 * </p>
 *
 * @author coderlee
 */
public class SugarDecorator extends CoffeeDecorator {

    public SugarDecorator(Coffee coffee) {
        super(coffee);
        this.description = "咖啡 + 糖";
    }

    @Override
    protected String getAddonDescription() {
        return "糖";
    }

    @Override
    protected double getAddonCost() {
        return 0.5;
    }
}
