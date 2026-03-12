package com.coderlee.designpattern.structural.decorator;

/**
 * 奶油装饰器（具体装饰器）
 * <p>
 * 为咖啡添加奶油配料
 * </p>
 *
 * @author coderlee
 */
public class WhippingDecorator extends CoffeeDecorator {

    public WhippingDecorator(Coffee coffee) {
        super(coffee);
        this.description = "咖啡 + 奶油";
    }

    @Override
    protected String getAddonDescription() {
        return "奶油";
    }

    @Override
    protected double getAddonCost() {
        return 2.5;
    }
}
