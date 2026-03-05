package com.coderlee.designpattern.creational.factory.factorymethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备工厂映射表
 * <p>
 * 提供基于类型字符串的工厂类注册和获取功能
 * 通过静态 Map 集中管理所有具体工厂实例，简化客户端代码
 * </p>
 * @author coderlee
 */
public class DeviceFactoryMap {
    /**
     * 静态工厂映射表
     * <p>
     * 存储设备类型字符串到对应工厂实例的映射关系
     * 使用 final 保证引用不可变，线程安全
     * </p>
     */
    private static final Map<String, DeviceFactory> FACTORY_MAP = new HashMap<>();
    
    // 静态代码块初始化，在类加载时执行一次
    static {
        // 注册电脑工厂，key 为"computer"
        FACTORY_MAP.put("computer", new ComputerFactory());
        // 注册手机工厂，key 为"phone"
        FACTORY_MAP.put("phone", new PhoneFactory());
    }
    
    /**
     * 根据类型获取对应的工厂实例
     * <p>
     * 从映射表中查找并返回指定类型的工厂对象
     * 如果类型不存在则抛出 IllegalArgumentException 异常
     * </p>
     * @param type 设备类型标识符（如："computer"、"phone"）
     * @return DeviceFactory 对应的工厂实例
     * @throws IllegalArgumentException 当传入的类型不在映射表中时抛出
     */
    public static DeviceFactory getFactory(String type) {
        // 检查映射表中是否包含指定的类型 key
        if (!FACTORY_MAP.containsKey(type)) {
            // 如果类型不存在，抛出非法参数异常，提示无效的类型信息
            throw new IllegalArgumentException("Invalid type: " + type);
        }
        // 从映射表中返回对应类型的工厂实例
        return FACTORY_MAP.get(type);
    }
}
