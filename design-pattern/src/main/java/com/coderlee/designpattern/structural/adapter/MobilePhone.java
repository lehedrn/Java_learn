package com.coderlee.designpattern.structural.adapter;

/**
 * 客户端：手机
 * <p>
 * 手机需要 5V 电压充电，无法直接使用 220V 电源
 * </p>
 *
 * @author coderlee
 */
public class MobilePhone {

    /**
     * 使用 5V 电源充电
     * @param power5V 5V 电源接口
     */
    public void charge(Power5V power5V) {
        int voltage = power5V.output5V();
        if (voltage == 5) {
            System.out.println("MobilePhone: 电压正常，开始充电 🔋");
        } else {
            System.out.println("MobilePhone: 电压异常，无法充电 ⚠️");
        }
    }
}
