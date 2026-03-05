package com.coderlee.designpattern.creational.factory.abstractfactory;

/**
 * 手机套装工厂类（具体工厂）
 * 
 * <p>实现 DeviceSetFactory 接口的具体工厂类，负责创建手机产品族中的所有产品。</p>
 * 
 * <p>在抽象工厂模式中的角色：</p>
 * <ul>
 *     <li>作为具体工厂，实现抽象工厂接口定义的所有方法</li>
 *     <li>创建一个完整的产品族：SmartPhone（智能手机） + PhoneCase（手机壳）</li>
 *     <li>确保创建的产品能够相互匹配和协作</li>
 * </ul>
 * 
 * <p>特点：</p>
 * <ul>
 *     <li>客户端只需要知道 PhoneSetFactory 即可获取一整套手机相关产品</li>
 *     <li>无需关心具体产品的创建细节和实现类名</li>
 *     <li>符合开闭原则：新增产品族只需添加新的工厂类</li>
 * </ul>
 * 
 * @author coderlee
 * @version 1.0
 * @see DeviceSetFactory
 * @see SmartPhone
 * @see PhoneCase
 */
public class PhoneSetFactory implements DeviceSetFactory {
    
    /**
     * 创建智能手机产品
     * 
     * <p>实例化并返回一个 SmartPhone 对象。</p>
     * <p>该方法是抽象工厂方法的具体实现，由工厂类决定创建的具体产品类型。</p>
     * 
     * @return Device 返回新创建的智能手机实例
     */
    @Override
    public Device createDevice() {
        // 创建并返回智能手机对象
        return new SmartPhone();
    }
    
    /**
     * 创建手机壳配件
     * 
     * <p>实例化并返回一个 PhoneCase 对象。</p>
     * <p>该配件与 createDevice() 创建的设备属于同一个产品族，可以配套使用。</p>
     * 
     * @return Accessory 返回新创建的手机壳实例
     */
    @Override
    public Accessory createAccessory() {
        // 创建并返回手机壳对象
        return new PhoneCase();
    }
}
