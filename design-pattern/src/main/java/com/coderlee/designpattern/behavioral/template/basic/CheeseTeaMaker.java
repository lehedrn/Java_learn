package com.coderlee.designpattern.behavioral.template.basic;

/**
 * 具体实现：芝士奶盖茶制作
 *
 * @author coderlee
 */
public class CheeseTeaMaker extends DrinkMaker {

    @Override
    protected void brewTea() {
        System.out.println("🍵 泡茶底：绿茶包浸泡 2 分钟");
    }

    @Override
    protected void addIngredients() {
        System.out.println("🧀 添加配料：芝士奶盖 + 鲜奶 + 少量糖");
    }
}
