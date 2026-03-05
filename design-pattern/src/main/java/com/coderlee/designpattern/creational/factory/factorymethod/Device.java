package com.coderlee.designpattern.creational.factory.factorymethod;

/**
 * 设备接口
 * <p>
 * 定义了所有具体设备产品需要实现的标准接口
 * 这是工厂方法模式中的抽象产品角色（Product）
 * </p>
 * @author coderlee
 */
public interface Device {
    /**
     * 设备操作方法
     * <p>
     * 定义设备的通用操作行为，由具体设备类实现各自的操作逻辑
     * </p>
     */
    void operate();
}
