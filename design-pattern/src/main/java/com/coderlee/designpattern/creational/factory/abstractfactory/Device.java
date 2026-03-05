package com.coderlee.designpattern.creational.factory.abstractfactory;

/**
 * 设备接口（抽象产品）
 * 
 * <p>定义了所有电子设备产品的公共接口，是抽象工厂模式中抽象产品的一种。</p>
 * <p>在抽象工厂模式中，一个工厂可以创建多种类型的产品，这些产品分为不同的等级结构：</p>
 * <ul>
 *     <li>Device（设备）：主产品接口</li>
 *     <li>Accessory（配件）：配套产品接口</li>
 * </ul>
 * 
 * @author coderlee
 * @version 1.0
 * @see Accessory
 * @see DeviceSetFactory
 */
public interface Device {
    
    /**
     * 设备操作的方法
     * 
     * <p>定义设备的核心操作行为，具体的实现由各个具体设备类完成。</p>
     * <p>例如：智能手机的 operate() 方法会执行手机的操作逻辑，笔记本电脑的 operate() 方法会执行电脑的操作逻辑。</p>
     */
    void operate();
}
