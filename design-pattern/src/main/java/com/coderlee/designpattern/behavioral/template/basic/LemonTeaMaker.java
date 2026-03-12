package com.coderlee.designpattern.behavioral.template.basic;

/**
 * 具体实现：柠檬茶制作
 *
 * @author coderlee
 */
public class LemonTeaMaker extends DrinkMaker {

    @Override
    protected void brewTea() {
        System.out.println("🍵 泡茶底：红茶包浸泡 5 分钟");
    }

    @Override
    protected void addIngredients() {
        System.out.println("🍋 添加配料：新鲜柠檬片 + 蜂蜜 + 冰块");
    }

    @Override
    protected void stir() {
        System.out.println("🥄 搅拌：轻轻搅拌，保留柠檬果肉");
    }
}
