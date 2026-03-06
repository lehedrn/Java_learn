package com.coderlee.designpattern.creational.prototype;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 怪物抽象基类
 * <p>
 * 实现了原型模式的接口，提供了怪物的基本属性和行为。
 * 作为所有具体怪物类的父类，定义了通用的原型框架。
 * </p>
 * 
 * @author coderlee
 * @version 1.0
 * @implements Prototype
 */
public abstract class Monster implements Prototype {
    /** 怪物类型名称 */
    protected String type;
    
    /** 生命值 */
    protected int hp;
    
    /** 掉落物品列表（引用类型，需要区分深浅拷贝） */
    protected List<DropItem> dropItems;
    
    /**
     * 默认构造方法
     * <p>
     * 用于初始化原型对象，通常在注册原型时使用。
     * 初始化掉落物品列表为空列表。
     * </p>
     */
    protected Monster() {
        // 默认构造，用于注册原型
        this.dropItems = new ArrayList<>();
    }
    
    /**
     * 拷贝构造方法
     * <p>
     * 根据 shallow 参数决定执行浅拷贝还是深拷贝。
     * 浅拷贝只复制基本类型和引用，深拷贝会递归复制所有引用对象。
     * </p>
     * 
     * @param other 要复制的原始怪物对象
     * @param shallow true 表示浅拷贝，false 表示深拷贝
     * 
     * 浅拷贝示例：this.dropItems = other.dropItems（共享同一列表）
     * 深拷贝示例：新建列表并复制每个物品（各自独立）
     */
    protected Monster(Monster other, boolean shallow) {
        this.type = other.type;
        this.hp = other.hp;
        if (shallow) {
            this.dropItems = other.dropItems;  // 浅拷贝：共享同一列表
        } else {
            // 深拷贝：新建列表 + 新建每个物品
            this.dropItems = new ArrayList<>();
            for (DropItem item : other.dropItems) {
                this.dropItems.add(new DropItem(item));  // 每个物品独立拷贝
            }
        }
    }
    
    /**
     * 配置怪物的变体
     * <p>
     * 抽象方法，由子类实现具体的变异逻辑。
     * 例如：精英怪、头目怪等特殊形态的属性调整。
     * </p>
     * 
     * @param variant 变体类型，如"普通"、"精英"、"头目"等
     * @param rand 随机数生成器，用于产生随机属性
     */
    public abstract void configure(String variant, Random rand);
    
    /**
     * 添加掉落物品
     * <p>
     * 向怪物的掉落列表中增加一个新物品。
     * 注意：浅拷贝情况下会影响所有共享该列表的实例。
     * </p>
     * 
     * @param name 物品名称
     * @param count 物品数量
     */
    public void addDropItem(String name, int count) {
        dropItems.add(new DropItem(name, count));
    }
    
    /**
     * 重写 toString 方法，格式化显示怪物信息
     * <p>
     * 返回包含怪物类型、生命值和掉落物品的字符串表示。
     * 便于调试和打印输出。
     * </p>
     * 
     * @return 怪物的字符串表示形式，格式："类型 (HP: 生命值) 掉落：[物品列表]"
     */
    @Override
    public String toString() {
        return String.format("%s (HP:%d) 掉落：%s", type, hp, dropItems);
    }
    
}
