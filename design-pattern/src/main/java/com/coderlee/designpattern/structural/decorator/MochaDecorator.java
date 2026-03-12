package com.coderlee.designpattern.structural.decorator;

/**
 * 摩卡装饰器（具体装饰器）
 * <p>
 * 为咖啡添加摩卡配料
 * </p>
 *
 * @author coderlee
 */
public class MochaDecorator extends CoffeeDecorator {

    public MochaDecorator(Coffee coffee) {
        super(coffee);
        this.description = "咖啡 + 摩卡";
    }

    @Override
    protected String getAddonDescription() {
        return "摩卡";
    }

    @Override
    protected double getAddonCost() {
        return 3.0;
    }
}
