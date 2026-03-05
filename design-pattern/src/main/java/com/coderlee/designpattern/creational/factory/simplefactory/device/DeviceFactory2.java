package com.coderlee.designpattern.creational.factory.simplefactory.device;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备工厂类（简单工厂模式 - Map 映射实现）
 * <p>
 * 使用 Map 数据结构存储设备类型与实例的映射关系，优化了对象查找效率。
 * 相比条件判断方式，代码结构更清晰，扩展性更好。
 * </p>
 * <p>
 * <b>特点：</b>
 * <ul>
 *     <li>使用静态代码块初始化设备映射表</li>
 *     <li>通过 HashMap 实现 O(1) 时间复杂度的快速查找</li>
 *     <li>避免了多重条件判断</li>
 * </ul>
 * </p>
 *
 * @author coderlee
 * @date 2026-03-05
 * @see Decive
 * @see Phone
 * @see Computer
 * @see DeviceFactory
 */
public class DeviceFactory2 {
    
    /**
     * 设备映射表，存储设备类型字符串与设备实例的对应关系
     * Key: 设备类型标识（大写），Value: 设备实例对象
     */
    private static final Map<String, Decive> DECIVE_MAP = new HashMap<>();

    /**
     * 静态代码块初始化设备映射表
     * <p>
     * 在类加载时执行一次，预先创建并注册所有支持的设备类型。
     * 使用大写键名确保类型匹配的兼容性。
     * </p>
     */
    static {
        // 注册手机设备类型
        DECIVE_MAP.put("PHONE", new Phone());
        // 注册电脑设备类型
        DECIVE_MAP.put("COMPUTER", new Computer());
    }

    /**
     * 根据设备类型创建设备实例
     * <p>
     * 从预定义的映射表中查找并返回对应的设备对象。
     * 自动将输入类型转换为大写进行匹配。
     * </p>
     *
     * @param type 设备类型标识符
     *             <ul>
     *                 <li>"phone"/"PHONE" - 返回手机设备对象</li>
     *                 <li>"computer"/"COMPUTER" - 返回电脑设备对象</li>
     *             </ul>
     * @return 返回创建的设备实例，如果类型不存在或输入无效则返回 null
     * @see Decive
     */
    public static Decive createDevice(String type) {
        // 参数有效性校验：检查 null 或空字符串
        if (null == type || type.isEmpty()) {
            return null;
        }
        // 转换为大写后从映射表中获取设备实例
        return DECIVE_MAP.get(type.toUpperCase());
    }
}
