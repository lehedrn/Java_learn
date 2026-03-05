package com.coderlee.designpattern.creational.factory.abstractfactory;

/**
 * 笔记本套装工厂类（具体工厂）
 * 
 * <p>实现 DeviceSetFactory 接口的具体工厂类，负责创建笔记本产品族中的所有产品。</p>
 * 
 * <p>在抽象工厂模式中的角色：</p>
 * <ul>
 *     <li>作为具体工厂，实现抽象工厂接口定义的所有方法</li>
 *     <li>创建一个完整的产品族：Laptop（笔记本电脑） + LaptopBag（电脑包）</li>
 *     <li>确保创建的产品能够相互匹配和协作</li>
 * </ul>
 * 
 * <p>特点：</p>
 * <ul>
 *     <li>客户端只需要知道 LaptopSetFactory 即可获取一整套笔记本相关产品</li>
 *     <li>无需关心具体产品的创建细节和实现类名</li>
 *     <li>符合开闭原则：新增产品族只需添加新的工厂类，无需修改现有代码</li>
 * </ul>
 * 
 * @author coderlee
 * @version 1.0
 * @see DeviceSetFactory
 * @see Laptop
 * @see LaptopBag
 */
public class LaptopSetFactory implements DeviceSetFactory {
    
    /**
     * 创建笔记本电脑产品
     * 
     * <p>实例化并返回一个 Laptop 对象。</p>
     * <p>该方法是抽象工厂方法的具体实现，由工厂类决定创建的具体产品类型。</p>
     * 
     * @return Device 返回新创建的笔记本电脑实例
     */
    @Override
    public Device createDevice() {
        // 创建并返回笔记本电脑对象
        return new Laptop();
    }
    
    /**
     * 创建电脑包配件
     * 
     * <p>实例化并返回一个 LaptopBag 对象。</p>
     * <p>该配件与 createDevice() 创建的设备属于同一个产品族，可以配套使用。</p>
     * 
     * @return Accessory 返回新创建的电脑包实例
     */
    @Override
    public Accessory createAccessory() {
        // 创建并返回电脑包对象
        return new LaptopBag();
    }
}
