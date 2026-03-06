package com.coderlee.designpattern.creational.prototype;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 怪物原型注册表
 * <p>
 * 实现了原型模式的核心管理类，维护了一个怪物原型的缓存池。
 * 通过预先注册的原型对象，可以快速克隆出新的怪物实例，避免重复创建。
 * 这是原型模式的典型应用场景：对象创建成本较高时，通过克隆现有对象来提高性能。
 * </p>
 * 
 * @author coderlee
 * @version 1.0
 */
public class MonsterRegistry {
    /** 
     * 原型缓存池
     * Key: 怪物类型的唯一标识（小写字符串）
     * Value: 对应的怪物原型实例
     */
    private static final Map<String, Monster> prototypes = new HashMap<>();
    
    // 静态代码块：在类加载时初始化原型
    static {
        prototypes.put("slime", new Slime());    // 注册史莱姆原型
        prototypes.put("goblin", new Goblin());  // 注册哥布林原型
    }
    
    /**
     * 创建怪物实例
     * <p>
     * 根据指定的类型和变体生成新的怪物实例。
     * 该方法结合了原型模式和工厂模式的特点：
     * 1. 从缓存池中获取原型对象
     * 2. 通过克隆创建新实例
     * 3. 应用随机变异配置
     * </p>
     * 
     * @param type 怪物类型标识，如"slime"、"goblin"（不区分大小写）
     * @param variant 变体类型，如"普通"、"精英"、"头目"等
     * @param useDeep true 表示使用深拷贝，false 表示使用浅拷贝
     * @param rand 随机数生成器，用于变异配置的随机性
     * @return 新创建的怪物实例
     * @throws IllegalArgumentException 当指定了未知的怪物类型时抛出
     */
    public static Monster create(String type, String variant, boolean useDeep, Random rand) {
        // 从原型池中获取对应的原型对象
        Monster proto = prototypes.get(type.toLowerCase());
        if (proto == null) {
            throw new IllegalArgumentException("未知怪物类型：" + type);
        }
        
        // 使用原型克隆：根据 useDeep 参数选择深拷贝或浅拷贝
        Monster instance = useDeep ? (Monster) proto.cloneDeep() : (Monster) proto.cloneShallow();
        
        // 配置随机变异：调整属性、添加特殊掉落等
        instance.configure(variant, rand);
        return instance;
    }
}
