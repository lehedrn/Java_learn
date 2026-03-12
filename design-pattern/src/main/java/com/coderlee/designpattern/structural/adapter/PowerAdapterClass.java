package com.coderlee.designpattern.structural.adapter;

/**
 * 适配器：电源转换器（类适配器模式）
 * <p>
 * 通过继承 Power220V 并实现 Power5V 接口
 * 将 220V 交流电转换为 5V 直流电
 * </p>
 * <p>
 * 类适配器模式特点：
 * - 使用继承，耦合度较高
 * - 只能适配一个类（因为 Java 单继承）
 * - 适配器可以直接调用被适配者的 protected 方法
 * </p>
 *
 * @author coderlee
 */
public class PowerAdapterClass extends Power220V implements Power5V {

    @Override
    public int output5V() {
        System.out.println("PowerAdapterClass: 开始转换电压...");
        // 调用被适配者的方法
        int sourceVoltage = output220V();
        System.out.println("PowerAdapterClass: 将 " + sourceVoltage + "V 转换为 5V");
        // 模拟电压转换
        return 5;
    }
}
