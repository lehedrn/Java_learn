package com.coderlee.designpattern.structural.flyweight.milktea;

import java.util.HashMap;
import java.util.Map;

/**
 * 享元工厂：奶茶工厂
 * <p>
 * 管理共享的奶茶类型对象
 * </p>
 *
 * @author coderlee
 */
public class TeaDrinkFactory {

    private static final Map<String, TeaDrink> drinkMap = new HashMap<>();

    /**
     * 获取奶茶类型
     * @param type 类型名称
     * @return 奶茶对象
     */
    public static TeaDrink getDrink(String type) {
        if (drinkMap.containsKey(type)) {
            System.out.println("  [复用] " + type + " 对象");
            return drinkMap.get(type);
        }

        TeaDrink drink;
        switch (type) {
            case "珍珠奶茶":
                drink = new BubbleMilkTea();
                break;
            case "芝士奶盖茶":
                drink = new CheeseMilkTea();
                break;
            case "水果茶":
                drink = new FruitTea();
                break;
            default:
                throw new IllegalArgumentException("不支持的奶茶类型：" + type);
        }

        drinkMap.put(type, drink);
        System.out.println("  [创建] " + type + " 对象");
        return drink;
    }

    /**
     * 获取已创建的奶茶类型数量
     * @return 数量
     */
    public static int getDrinkCount() {
        return drinkMap.size();
    }
}
