package com.coderlee.designpattern.creational.factory.simplefactory.device;

/**
 * 客户端测试类
 * <p>
 * 演示简单工厂模式的两种实现方式：
 * <ol>
 *     <li>{@link DeviceFactory} - 基于条件判断的工厂实现</li>
 *     <li>{@link DeviceFactory2} - 基于 Map 映射的工厂实现</li>
 * </ol>
 * 通过调用不同的工厂方法，展示如何使用简单工厂模式创建和使用设备对象。
 * </p>
 *
 * @author coderlee
 * @date 2026-03-05
 * @see DeviceFactory
 * @see DeviceFactory2
 * @see Decive
 */
public class Client {
    
    /**
     * 主方法 - 程序入口
     * <p>
     * 依次调用两个工厂方法的测试，展示不同实现方式的效果。
     * </p>
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // 测试第一种工厂实现（条件判断方式）
        factory1();
        // 测试第二种工厂实现（Map 映射方式）
        factory2();
    }

    /**
     * 测试 DeviceFactory2（Map 映射方式的工厂）
     * <p>
     * 演示如何使用基于 Map 的工厂创建手机和电脑设备，并调用它们的操作方法。
     * 展示了工厂模式的核心优势：客户端无需直接使用 new 关键字创建对象。
     * </p>
     */
    private static void factory2() {
        // 通过工厂方法创建手机设备实例
        Decive phone = DeviceFactory2.createDevice("phone");
        // 调用手机的通用操作方法
        phone.operate();
        
        // 通过工厂方法创建电脑设备实例
        Decive computer = DeviceFactory2.createDevice("computer");
        // 调用电脑的通用操作方法
        computer.operate();
    }

    /**
     * 测试 DeviceFactory（条件判断方式的工厂）
     * <p>
     * 演示如何使用基于条件判断的工厂创建手机和电脑设备，并调用它们的操作方法。
     * 类型参数支持不区分大小写的匹配方式。
     * </p>
     */
    private static void factory1() {
        // 通过工厂方法创建手机设备实例（支持大小写混合）
        Decive phone = DeviceFactory.createDevice("phone");
        // 调用手机的通用操作方法
        phone.operate();
        
        // 通过工厂方法创建电脑设备实例（支持大小写混合）
        Decive computer = DeviceFactory.createDevice("computer");
        // 调用电脑的通用操作方法
        computer.operate();
    }
}
