package com.coderlee.designpattern.structural.adapter;

/**
 * 适配器：电源转换器（对象适配器模式）⭐推荐
 * <p>
 * 通过组合 Power220V 并实现 Power5V 接口
 * 将 220V 交流电转换为 5V 直流电
 * </p>
 * <p>
 * 对象适配器模式特点：
 * - 使用组合，耦合度低
 * - 可以适配多个类（更灵活）
 * - 符合组合复用原则
 * - 更符合开闭原则
 * </p>
 *
 * @author coderlee
 */
public class PowerAdapter implements Power5V {

    /**
     * 持有被适配者的引用
     */
    private Power220V power220V;

    public PowerAdapter(Power220V power220V) {
        this.power220V = power220V;
    }

    @Override
    public int output5V() {
        System.out.println("PowerAdapter: 开始转换电压...");
        // 调用被适配者的方法
        int sourceVoltage = power220V.output220V();
        System.out.println("PowerAdapter: 将 " + sourceVoltage + "V 转换为 5V");
        // 模拟电压转换
        return 5;
    }
}
