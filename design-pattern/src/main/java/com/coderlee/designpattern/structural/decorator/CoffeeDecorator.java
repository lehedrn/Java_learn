package com.coderlee.designpattern.structural.decorator;

/**
 * 咖啡装饰器抽象类（抽象装饰器）
 * <p>
 * 继承自 Coffee 类，持有一个 Coffee 对象的引用
 * 将请求委托给被装饰的咖啡对象，子类可以添加额外的行为
 * </p>
 *
 * @author coderlee
 */
public abstract class CoffeeDecorator extends Coffee {

    /**
     * 被装饰的咖啡对象
     */
    protected Coffee decoratedCoffee;

    /**
     * 构造方法，注入被装饰的咖啡
     * @param coffee 被装饰的咖啡对象
     */
    public CoffeeDecorator(Coffee coffee) {
        this.decoratedCoffee = coffee;
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + " + " + getAddonDescription();
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + getAddonCost();
    }

    /**
     * 获取添加配料的描述
     * @return 配料描述
     */
    protected abstract String getAddonDescription();

    /**
     * 获取添加配料的价格
     * @return 配料价格
     */
    protected abstract double getAddonCost();
}
