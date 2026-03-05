package com.coderlee.designpattern.creational.factory.factorymethod;

/**
 * 设备工厂接口
 * <p>
 * 定义了工厂方法的标准，所有具体工厂类都需要实现此接口
 * 这是工厂方法模式中的抽象工厂角色（Creator）
 * </p>
 * @author coderlee
 */
public interface DeviceFactory {
    /**
     * 创建设备实例
     * <p>
     * 工厂方法，用于创建具体的设备产品对象
     * </p>
     * @return Device 创建的设备实例
     */
    Device createDevice();
}
