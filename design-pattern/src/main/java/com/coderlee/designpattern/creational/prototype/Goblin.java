package com.coderlee.designpattern.creational.prototype;

import java.util.Random;

/**
 * 哥布林怪物类
 * <p>
 * Monster 的具体实现类，代表一种基础怪物类型。
 * 具有固定的基础属性和掉落物品，支持通过配置方法生成变体（如头目）。
 * </p>
 * 
 * @author coderlee
 * @version 1.0
 * @extends Monster
 */
public class Goblin extends Monster {
    /**
     * 默认构造方法
     * <p>
     * 初始化哥布林的基础属性：
     * - 类型名称：哥布林
     * - 生命值：120
     * - 掉落物品：生锈短剑 x1、金币 x25
     * </p>
     */
    public Goblin() {
        this.type = "哥布林";
        this.hp = 120;
        this.dropItems.add(new DropItem("生锈短剑", 1));  // 基础武器掉落
        this.dropItems.add(new DropItem("金币", 25));      // 基础货币掉落
    }
    
    /**
     * 私有的拷贝构造方法
     * <p>
     * 仅在类内部使用，用于实现 cloneShallow 和 cloneDeep 方法。
     * 防止外部直接调用，保证克隆方法的统一性。
     * </p>
     * 
     * @param other 要复制的哥布林对象
     * @param shallow true 表示浅拷贝，false 表示深拷贝
     */
    private Goblin(Goblin other, boolean shallow) {
        super(other, shallow);
    }
    
    /**
     * 实现浅拷贝方法
     * <p>
     * 创建一个共享引用类型字段的哥布林副本。
     * 适用于不修改引用类型字段的场景。
     * </p>
     * 
     * @return 当前哥布林的浅拷贝副本
     */
    @Override
    public Prototype cloneShallow() {
        return new Goblin(this, true);
    }
    
    /**
     * 实现深拷贝方法
     * <p>
     * 创建一个完全独立的哥布林副本，包括所有引用类型字段。
     * 修改深拷贝后的对象不会影响原始对象。
     * </p>
     * 
     * @return 当前哥布林的深拷贝副本
     */
    @Override
    public Prototype cloneDeep() {
        return new Goblin(this, false);
    }
    
    /**
     * 配置哥布林的变体
     * <p>
     * 实现父类的抽象方法，支持生成"头目"变体：
     * - 生命值提升 250%~400%
     * - 类型名称变为"哥布林头目"
     * - 50% 概率额外掉落"头目徽章"
     * </p>
     * 
     * @param variant 变体类型，仅支持"头目"
     * @param rand 随机数生成器
     */
    @Override
    public void configure(String variant, Random rand) {
        if ("头目".equals(variant)) {
            double mod = 2.5 + rand.nextDouble() * 1.5;  // 250% ~ 400%
            this.hp = (int) (this.hp * mod);
            this.type = "哥布林头目";
            if (rand.nextDouble() < 0.5) {  // 50% 概率加额外掉落
                addDropItem("头目徽章", 1);
            }
        }
    }
}
