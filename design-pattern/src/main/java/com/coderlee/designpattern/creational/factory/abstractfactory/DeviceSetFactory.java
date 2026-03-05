package com.coderlee.designpattern.creational.factory.abstractfactory;

/**
 * 设备套装工厂接口（抽象工厂）
 * 
 * <p>这是抽象工厂模式的核心接口，定义了创建一系列相关或相互依赖对象的接口。</p>
 * <p>抽象工厂模式的特点：</p>
 * <ul>
 *     <li>一个工厂可以创建多个不同种类的产品（如 Device 和 Accessory）</li>
 *     <li>这些产品属于同一个产品族（如手机套装、笔记本套装）</li>
 *     <li>客户端通过抽象接口获取产品，无需关心具体实现</li>
 * </ul>
 * 
 * <p>使用场景：</p>
 * <ul>
 *     <li>系统需要独立于产品的创建、组合和表示</li>
 *     <li>系统要由多个产品系列中的一个来配置</li>
 *     <li>当需要强调一系列相关的产品对象的设计以便进行联合使用时</li>
 * </ul>
 * 
 * @author coderlee
 * @version 1.0
 * @see PhoneSetFactory 具体工厂：手机套装工厂
 * @see LaptopSetFactory 具体工厂：笔记本套装工厂
 */
public interface DeviceSetFactory {
    
    /**
     * 创建设备产品
     * 
     * <p>生产一个具体的设备对象，由具体工厂实现决定创建哪种设备。</p>
     * <p>例如：PhoneSetFactory 创建 SmartPhone，LaptopSetFactory 创建 Laptop。</p>
     * 
     * @return Device 设备对象实例
     */
    Device createDevice();
    
    /**
     * 创建配件产品
     * 
     * <p>生产一个具体的配件对象，由具体工厂实现决定创建哪种配件。</p>
     * <p>例如：PhoneSetFactory 创建 PhoneCase，LaptopSetFactory 创建 LaptopBag。</p>
     * 
     * @return Accessory 配件对象实例
     */
    Accessory createAccessory();
}
