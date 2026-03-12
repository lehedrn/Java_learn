package com.coderlee.designpattern.behavioral.template.basic;

/**
 * 抽象类：奶茶制作模板
 * <p>
 * 定义制作奶茶的标准流程（算法骨架）
 * </p>
 *
 * @author coderlee
 */
public abstract class DrinkMaker {

    /**
     * 模板方法：制作饮料的完整流程
     * <p>
     * 使用 final 防止子类修改算法骨架
     * </p>
     */
    public final void makeDrink() {
        System.out.println("=== 开始制作饮料 ===");
        boilWater();           // 烧水
        brewTea();             // 泡茶底（抽象）
        addIngredients();      // 添加配料（抽象）
        stir();                // 搅拌
        serve();               // 装杯
        System.out.println("=== 饮料制作完成 ===\n");
    }

    /**
     * 具体方法：烧水
     */
    protected void boilWater() {
        System.out.println("🔥 烧水：加热至 95°C");
    }

    /**
     * 抽象方法：泡茶底
     * <p>
     * 由子类实现具体的泡茶方式
     * </p>
     */
    protected abstract void brewTea();

    /**
     * 抽象方法：添加配料
     * <p>
     * 由子类实现具体的配料
     * </p>
     */
    protected abstract void addIngredients();

    /**
     * 具体方法：搅拌
     */
    protected void stir() {
        System.out.println("🥄 搅拌：使配料混合均匀");
    }

    /**
     * 具体方法：装杯
     */
    protected void serve() {
        System.out.println("🥤 装杯：封盖，准备出杯");
    }
}
