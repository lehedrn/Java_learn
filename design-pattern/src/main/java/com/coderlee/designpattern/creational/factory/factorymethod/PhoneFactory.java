package com.coderlee.designpattern.creational.factory.factorymethod;

import lombok.extern.slf4j.Slf4j;

/**
 * 手机工厂类
 * <p>
 * 实现了 DeviceFactory 接口的具体工厂类，专门用于创建手机设备实例
 * 这是工厂方法模式中的具体工厂角色（ConcreteCreator）
 * </p>
 * @author coderlee
 */
@Slf4j
public class PhoneFactory implements DeviceFactory {
    /**
     * 创建手机设备实例
     * <p>
     * 实现工厂方法，负责创建并返回 Phone 类型的设备实例
     * </p>
     * @return Device 创建的手机设备实例
     */
    @Override
    public Device createDevice() {
        // 记录创建手机的日志信息
        log.info("create phone...");
        // 创建并返回新的 Phone 实例
        return new Phone();
    }
}
