package com.coderlee.designpattern.creational.factory.factorymethod;

/**
 * 工厂方法模式客户端测试类
 * <p>
 * 演示如何使用工厂方法模式创建设备对象并调用其方法
 * 展示了两种使用方式：
 * 1. 直接实例化具体工厂类
 * 2. 通过工厂映射表获取工厂实例
 * </p>
 * @author coderlee
 */
public class Client {
    /**
     * 主方法 - 程序入口点
     * <p>
     * 依次执行两个演示方法，展示工厂方法模式的使用
     * </p>
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 调用第一个演示方法：直接使用具体工厂类
        demo1();
        // 调用第二个演示方法：通过工厂映射表获取工厂
        demo2();
    }

    /**
     * 演示方法 2 - 使用工厂映射表
     * <p>
     * 通过 DeviceFactoryMap 获取工厂实例，降低客户端与具体工厂类的耦合
     * 这种方式更加灵活，便于扩展新的工厂类
     * 这种方式可以避免一堆的if-else 语句
     * </p>
     */
    private static void demo2() {
        // 从工厂映射表获取手机工厂实例
        DeviceFactory phoneFactory = DeviceFactoryMap.getFactory("phone");
        // 从工厂映射表获取电脑工厂实例
        DeviceFactory computerFactory = DeviceFactoryMap.getFactory("computer");
        // 使用手机工厂创建手机设备实例
        Device phone = phoneFactory.createDevice();
        // 使用电脑工厂创建电脑设备实例
        Device computer = computerFactory.createDevice();
        // 调用手机设备的操作方法
        phone.operate();
        // 调用电脑设备的操作方法
        computer.operate();
    }

    /**
     * 演示方法 1 - 直接使用具体工厂类
     * <p>
     * 直接实例化具体的工厂类来创建产品对象
     * 这种方式简单直观，但客户端代码与具体工厂类耦合度较高
     * </p>
     */
    private static void demo1() {
        // 直接创建手机工厂实例
        DeviceFactory phoneFactory = new PhoneFactory();
        // 直接创建电脑工厂实例
        DeviceFactory computerFactory = new ComputerFactory();
        // 使用手机工厂创建手机设备实例
        Device phone = phoneFactory.createDevice();
        // 使用电脑工厂创建电脑设备实例
        Device computer = computerFactory.createDevice();
        // 调用手机设备的操作方法
        phone.operate();
        // 调用电脑设备的操作方法
        computer.operate();
    }
}
