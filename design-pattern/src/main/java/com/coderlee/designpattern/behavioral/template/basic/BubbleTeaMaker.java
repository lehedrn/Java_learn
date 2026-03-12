package com.coderlee.designpattern.behavioral.template.basic;

/**
 * 具体实现：珍珠奶茶制作
 *
 * @author coderlee
 */
public class BubbleTeaMaker extends DrinkMaker {

    @Override
    protected void brewTea() {
        System.out.println("🍵 泡茶底：红茶包浸泡 3 分钟");
    }

    @Override
    protected void addIngredients() {
        System.out.println("🧋 添加配料：珍珠 + 牛奶 + 糖浆");
    }
}
