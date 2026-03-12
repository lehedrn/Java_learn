package com.coderlee.designpattern.behavioral.template.basic;

/**
 * 基本模板方法演示 - 制作奶茶
 * <p>
 * 演示场景：
 * 1. 奶茶店有多种饮品制作配方
 * 2. 所有饮品遵循相同的制作流程
 * 3. 每种饮品的泡茶和配料不同
 * </p>
 *
 * @author coderlee
 */
public class BasicTemplateDemo {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║     模板方法模式 - 基本模板方法                   ║");
        System.out.println("║     场景：奶茶店制作饮料                         ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        // 制作珍珠奶茶
        System.out.println("【订单 1】珍珠奶茶\n");
        DrinkMaker bubbleTea = new BubbleTeaMaker();
        bubbleTea.makeDrink();

        // 制作芝士奶盖茶
        System.out.println("【订单 2】芝士奶盖茶\n");
        DrinkMaker cheeseTea = new CheeseTeaMaker();
        cheeseTea.makeDrink();

        // 制作柠檬茶
        System.out.println("【订单 3】柠檬茶\n");
        DrinkMaker lemonTea = new LemonTeaMaker();
        lemonTea.makeDrink();

        System.out.println("========== 演示结束 ==========");
    }
}
