package com.coderlee.designpattern.creational.factory.abstractfactory;

import lombok.extern.slf4j.Slf4j;

/**
 * 电脑包类（具体产品）
 * 
 * <p>实现 Accessory 接口的具体产品类，代表笔记本电脑配件中的电脑包。</p>
 * <p>在抽象工厂模式中，电脑包与笔记本电脑属于同一个产品族：</p>
 * <ul>
 *     <li>LaptopBag（电脑包）配合 Laptop（笔记本电脑）使用</li>
 *     <li>两者都由 LaptopSetFactory（笔记本套装工厂）创建</li>
 * </ul>
 * 
 * <p>设计要点：</p>
 * <ul>
 *     <li>实现 Accessory 接口定义的 use() 方法</li>
 *     <li>体现配件与设备的配套关系，提供保护和便携功能</li>
 * </ul>
 * 
 * @author coderlee
 * @version 1.0
 * @see Accessory
 * @see LaptopSetFactory
 * @see Laptop
 */
@Slf4j
public class LaptopBag implements Accessory {
    
    /**
     * 实现电脑包的使用方法
     * 
     * <p>该方法展示了电脑包的具体使用方式，包括收纳电脑、保护设备、便于携带等功能。</p>
     * <p>使用 Lombok 的 @Slf4j 注解自动注入日志记录器，用于输出使用日志。</p>
     */
    @Override
    public void use() {
        // 记录使用电脑包的日志信息
        log.info("use laptop bag");
    }
}
