package com.coderlee.designpattern.structural.adapter;

/**
 * 目标接口：中国天气服务
 * <p>
 * 客户端期望的接口标准
 * 返回华氏温度和中文消息
 * </p>
 *
 * @author coderlee
 */
public interface ChinaWeatherService {
    /**
     * 获取温度（华氏度）
     * @return 华氏温度
     */
    double getTemperatureFahrenheit();

    /**
     * 获取天气描述（中文）
     * @return 中文天气描述
     */
    String getDescriptionChinese();
}
