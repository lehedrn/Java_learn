package com.coderlee.designpattern.creational.factory.abstractfactory;

import lombok.extern.slf4j.Slf4j;

/**
 * 手机壳类（具体产品）
 * 
 * <p>实现 Accessory 接口的具体产品类，代表手机配件中的手机壳。</p>
 * <p>在抽象工厂模式中，手机壳与智能手机属于同一个产品族：</p>
 * <ul>
 *     <li>PhoneCase（手机壳）配合 SmartPhone（智能手机）使用</li>
 *     <li>两者都由 PhoneSetFactory（手机套装工厂）创建</li>
 * </ul>
 * 
 * <p>设计要点：</p>
 * <ul>
 *     <li>实现 Accessory 接口定义的 use() 方法</li>
 *     <li>体现配件与设备的配套关系</li>
 * </ul>
 * 
 * @author coderlee
 * @version 1.0
 * @see Accessory
 * @see PhoneSetFactory
 * @see SmartPhone
 */
@Slf4j
public class PhoneCase implements Accessory {
    
    /**
     * 实现手机壳的使用方法
     * 
     * <p>该方法展示了手机壳的具体使用方式，包括保护手机、装饰等功能。</p>
     * <p>使用 Lombok 的 @Slf4j 注解自动注入日志记录器，用于输出使用日志。</p>
     */
    @Override
    public void use() {
        // 记录使用手机壳的日志信息
        log.info("use phonecase");
    }
}
