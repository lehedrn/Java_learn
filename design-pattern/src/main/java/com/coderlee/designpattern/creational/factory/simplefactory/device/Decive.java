package com.coderlee.designpattern.creational.factory.simplefactory.device;

/**
 * 设备接口（简单工厂模式）
 * <p>
 * 定义所有设备类的统一行为规范，作为简单工厂模式中的抽象产品角色。
 * 所有具体的设备类都需要实现此接口，提供各自的操作实现。
 * </p>
 *
 * @author coderlee
 * @date 2026-03-05
 * @see Phone
 * @see Computer
 */
public interface Decive {
    /**
     * 执行设备操作
     * <p>
     * 不同设备类型有不同的操作行为，由具体实现类定义。
     * 例如：手机可能执行通话、上网等操作；电脑可能执行办公、娱乐等操作。
     * </p>
     */
    void operate();
}
