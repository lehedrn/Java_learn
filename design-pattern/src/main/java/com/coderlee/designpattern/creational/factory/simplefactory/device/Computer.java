package com.coderlee.designpattern.creational.factory.simplefactory.device;

import lombok.extern.slf4j.Slf4j;

/**
 * 电脑设备类（具体产品角色）
 * <p>
 * 实现设备接口，表示电脑这一具体设备类型。
 * 在简单工厂模式中作为具体产品，由工厂类根据客户端请求创建。
 * </p>
 *
 * @author coderlee
 * @date 2026-03-05
 * @see Decive
 * @see DeviceFactory
 * @see DeviceFactory2
 */
@Slf4j
public class Computer implements Decive {
    /**
     * 执行电脑设备的操作
     * <p>
     * 实现 {@link Decive#operate()} 方法，输出电脑操作的日志信息。
     * 实际应用中可以扩展为运行程序、处理文档、播放视频等具体功能。
     * </p>
     */
    @Override
    public void operate() {
        // 记录电脑操作日志
        log.info("computer operate...");
    }
}
