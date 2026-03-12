package com.coderlee.designpattern.structural.adapter;

/**
 * 被适配者：第三方天气服务（日本）
 * <p>
 * 返回摄氏度和日文消息
 * </p>
 *
 * @author coderlee
 */
public class JapanWeatherService {

    /**
     * 获取温度（摄氏度）
     * @return 摄氏温度
     */
    public double getTemperatureCelsius() {
        System.out.println("JapanWeatherService: 获取摄氏温度");
        return 25.0;
    }

    /**
     * 获取天气描述（日文）
     * @return 日文天气描述
     */
    public String getDescriptionJapanese() {
        System.out.println("JapanWeatherService: 获取日文天气描述");
        return "晴れ"; // 晴天
    }
}
