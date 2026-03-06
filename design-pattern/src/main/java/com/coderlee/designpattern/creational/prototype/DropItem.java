package com.coderlee.designpattern.creational.prototype;

/**
 * 掉落物品类
 * <p>
 * 表示怪物掉落的物品，包含物品名称和数量。
 * 支持通过拷贝构造方法创建副本，用于实现深拷贝功能。
 * </p>
 * 
 * @author coderlee
 * @version 1.0
 */
public class DropItem {
    /** 物品名称 */
    private String name;
    
    /** 物品数量 */
    private int count;
    
    /**
     * 构造方法
     * <p>
     * 使用指定的名称和数量创建一个新的掉落物品。
     * </p>
     * 
     * @param name 物品名称，如"金币"、"武器"等
     * @param count 物品数量，必须为正整数
     */
    public DropItem(String name, int count) {
        this.name = name;
        this.count = count;
    }
    
    /**
     * 拷贝构造方法
     * <p>
     * 基于另一个 DropItem 对象创建副本。
     * 由于 String 是不可变类，int 是基本类型，因此直接赋值即可实现深拷贝。
     * </p>
     * 
     * @param other 要复制的掉落物品对象
     */
    public DropItem(DropItem other) {
        this.name = other.name;
        this.count = other.count;
    }
    
    /**
     * 重写 toString 方法，格式化显示物品信息
     * <p>
     * 返回包含物品名称和数量的字符串表示。
     * 便于调试和打印输出。
     * </p>
     * 
     * @return 掉落物品的字符串表示形式，格式："DropItem{name='名称', count=数量}"
     */
    @Override
    public String toString() {
        return "DropItem{" +
                "name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
