package com.coderlee.designpattern.structural.decorator;

/**
 * 牛奶装饰器（具体装饰器）
 * <p>
 * 为咖啡添加牛奶配料
 * </p>
 *
 * @author coderlee
 */
public class MilkDecorator extends CoffeeDecorator {

    public MilkDecorator(Coffee coffee) {
        super(coffee);
        this.description = "咖啡 + 牛奶";
    }

    @Override
    protected String getAddonDescription() {
        return "牛奶";
    }

    @Override
    protected double getAddonCost() {
        return 2.0;
    }
}
