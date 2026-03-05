package com.coderlee.designpattern.creational.factory.factorymethod;

import lombok.extern.slf4j.Slf4j;

/**
 * 手机类
 * <p>
 * 实现了 Device 接口的具体产品类，代表手机这种设备
 * 这是工厂方法模式中的具体产品角色（ConcreteProduct）
 * </p>
 * @author coderlee
 */
@Slf4j
public class Phone implements Device {
    /**
     * 实现设备的操作方法
     * <p>
     * 具体实现手机设备的操作逻辑，通过日志记录操作信息
     * </p>
     */
    @Override
    public void operate() {
        // 使用 Lombok 的@Slf4j 注解生成的 log 对象记录日志
        log.info("phone operate...");
    }
}
