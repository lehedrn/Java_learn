package com.coderlee.designpattern.structural.adapter;

/**
 * 被适配者：220V 交流电
 * <p>
 * 这是现有的类，输出的是 220V 交流电
 * 但客户端需要的是 5V 直流电，所以需要适配器
 * </p>
 *
 * @author coderlee
 */
public class Power220V {

    /**
     * 输出 220V 交流电
     * @return 电压值
     */
    public int output220V() {
        System.out.println("Power220V: 输出 220V 交流电");
        return 220;
    }
}
