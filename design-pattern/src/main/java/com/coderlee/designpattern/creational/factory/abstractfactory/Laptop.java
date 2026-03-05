package com.coderlee.designpattern.creational.factory.abstractfactory;

import lombok.extern.slf4j.Slf4j;

/**
 * 笔记本电脑类（具体产品）
 * 
 * <p>实现 Device 接口的具体产品类，代表笔记本电脑这一具体设备类型。</p>
 * <p>在抽象工厂模式中，笔记本电脑由对应的具体工厂创建：</p>
 * <ul>
 *     <li>Laptop 由 LaptopSetFactory 创建</li>
 *     <li>与 LaptopBag（电脑包）属于同一个产品族</li>
 * </ul>
 * 
 * <p>职责：</p>
 * <ul>
 *     <li>实现 Device 接口定义的 operate() 方法</li>
 *     <li>提供笔记本电脑特有的操作逻辑（如运行程序、处理文档等）</li>
 * </ul>
 * 
 * @author coderlee
 * @version 1.0
 * @see Device
 * @see LaptopSetFactory
 * @see LaptopBag
 */
@Slf4j
public class Laptop implements Device {
    
    /**
     * 实现笔记本电脑的操作逻辑
     * 
     * <p>该方法展示了笔记本电脑的具体操作行为，包括开机、运行软件、多任务处理等功能。</p>
     * <p>使用 Lombok 的 @Slf4j 注解自动注入日志记录器，用于输出操作日志。</p>
     */
    @Override
    public void operate() {
        // 记录笔记本电脑操作的日志信息
        log.info("laptop operate...");
    }
}
