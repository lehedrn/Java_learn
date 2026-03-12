package com.coderlee.designpattern.structural.decorator;

/**
 * 咖啡组件接口（抽象类）
 * <p>
 * 定义了咖啡的基本方法，所有具体咖啡和装饰器都要实现这些方法
 * </p>
 *
 * @author coderlee
 */
public abstract class Coffee {

    /**
     * 咖啡描述
     */
    protected String description = "Unknown Coffee";

    /**
     * 获取咖啡的描述
     * @return 咖啡描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 获取咖啡的价格
     * @return 咖啡价格
     */
    public abstract double getCost();
}
