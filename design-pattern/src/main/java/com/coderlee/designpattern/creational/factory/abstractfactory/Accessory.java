package com.coderlee.designpattern.creational.factory.abstractfactory;

/**
 * 配件接口（抽象产品）
 * 
 * <p>定义了所有设备配件产品的公共接口，是抽象工厂模式中抽象产品的另一种。</p>
 * <p>配件与设备形成产品族，同一个产品族的设备和配件相互匹配。</p>
 * <p>例如：手机配手机壳，笔记本配电脑包。</p>
 * 
 * @author coderlee
 * @version 1.0
 * @see Device
 * @see DeviceSetFactory
 */
public interface Accessory {
    
    /**
     * 配件使用的方法
     * 
     * <p>定义配件的使用行为，具体的实现由各个具体配件类完成。</p>
     * <p>例如：手机壳的 use() 方法会展示如何使用手机壳，电脑包的 use() 方法会展示如何使用电脑包。</p>
     */
    void use();
}
