package com.coderlee.designpattern.structural.adapter;

/**
 * 目标接口：5V 电源接口
 * <p>
 * 这是客户端期望使用的接口标准
 * 比如手机充电器输出的 5V 电压
 * </p>
 *
 * @author coderlee
 */
public interface Power5V {
    /**
     * 输出 5V 电压
     * @return 电压值
     */
    int output5V();
}
