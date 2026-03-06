package com.coderlee.designpattern.creational.prototype;

import java.util.Random;

/**
 * 史莱姆怪物类
 * <p>
 * Monster 的具体实现类，代表一种基础怪物类型。
 * 具有较弱的属性，支持通过配置方法生成精英变体。
 * </p>
 * 
 * @author coderlee
 * @version 1.0
 * @extends Monster
 */
public class Slime extends Monster {
    /**
     * 默认构造方法
     * <p>
     * 初始化史莱姆的基础属性：
     * - 类型名称：史莱姆
     * - 生命值：60
     * - 掉落物品：史莱姆胶 x1、金币 x8
     * </p>
     */
    public Slime() {
        this.type = "史莱姆";
        this.hp = 60;
        this.dropItems.add(new DropItem("史莱姆胶", 1));  // 基础材料掉落
        this.dropItems.add(new DropItem("金币", 8));       // 基础货币掉落
    }
    
    /**
     * 私有的拷贝构造方法
     * <p>
     * 仅在类内部使用，用于实现 cloneShallow 和 cloneDeep 方法。
     * 防止外部直接调用，保证克隆方法的统一性。
     * </p>
     * 
     * @param other 要复制的史莱姆对象
     * @param shallow true 表示浅拷贝，false 表示深拷贝
     */
    private Slime(Slime other, boolean shallow) {
        super(other, shallow);
    }
    
    /**
     * 实现浅拷贝方法
     * <p>
     * 创建一个共享引用类型字段的史莱姆副本。
     * 适用于不修改引用类型字段的场景。
     * </p>
     * 
     * @return 当前史莱姆的浅拷贝副本
     */
    @Override
    public Prototype cloneShallow() {
        return new Slime(this, true);
    }
    
    /**
     * 实现深拷贝方法
     * <p>
     * 创建一个完全独立的史莱姆副本，包括所有引用类型字段。
     * 修改深拷贝后的对象不会影响原始对象。
     * </p>
     * 
     * @return 当前史莱姆的深拷贝副本
     */
    @Override
    public Prototype cloneDeep() {
        return new Slime(this, false);
    }
    
    /**
     * 配置史莱姆的变体
     * <p>
     * 实现父类的抽象方法，支持生成"精英"变体：
     * - 生命值提升 120%~180%
     * - 类型名称前缀加上"精英"
     * - 30% 概率额外掉落稀有材料"稀有粘液"
     * </p>
     * 
     * @param variant 变体类型，仅支持"精英"
     * @param rand 随机数生成器
     */
    @Override
    public void configure(String variant, Random rand) {
        if ("精英".equals(variant)) {
            double mod = 1.2 + rand.nextDouble() * 0.6;  // 120% ~ 180%
            this.hp = (int) (this.hp * mod);
            this.type = "精英" + this.type;
            if (rand.nextDouble() < 0.3) {  // 30% 概率加稀有掉落
                addDropItem("稀有粘液", 1);
            }
        }
    }
}
