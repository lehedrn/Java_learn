package com.coderlee.designpattern.creational.factory.abstractfactory;

import lombok.extern.slf4j.Slf4j;

/**
 * 智能手机类（具体产品）
 * 
 * <p>实现 Device 接口的具体产品类，代表手机这一具体设备类型。</p>
 * <p>在抽象工厂模式中，具体产品由对应的具体工厂创建：</p>
 * <ul>
 *     <li>SmartPhone 由 PhoneSetFactory 创建</li>
 *     <li>与 PhoneCase（手机壳）属于同一个产品族</li>
 * </ul>
 * 
 * <p>职责：</p>
 * <ul>
 *     <li>实现 Device 接口定义的 operate() 方法</li>
 *     <li>提供智能手机特有的操作逻辑</li>
 * </ul>
 * 
 * @author coderlee
 * @version 1.0
 * @see Device
 * @see PhoneSetFactory
 * @see PhoneCase
 */
@Slf4j
public class SmartPhone implements Device {
    
    /**
     * 实现智能手机的操作逻辑
     * 
     * <p>该方法展示了智能手机的具体操作行为，包括启动应用、通话、上网等功能。</p>
     * <p>使用 Lombok 的 @Slf4j 注解自动注入日志记录器，用于输出操作日志。</p>
     */
    @Override
    public void operate() {
        // 记录智能手机操作的日志信息
        log.info("smartphone operate...");
    }
}
