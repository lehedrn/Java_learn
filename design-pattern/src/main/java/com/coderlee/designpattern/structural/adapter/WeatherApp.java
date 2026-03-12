package com.coderlee.designpattern.structural.adapter;

/**
 * 客户端：天气显示应用
 * <p>
 * 期望使用中国天气服务接口
 * </p>
 *
 * @author coderlee
 */
public class WeatherApp {

    /**
     * 显示天气信息
     * @param weatherService 天气服务接口
     */
    public void displayWeather(ChinaWeatherService weatherService) {
        double temp = weatherService.getTemperatureFahrenheit();
        String desc = weatherService.getDescriptionChinese();
        System.out.println("WeatherApp: 当前天气 " + desc + "，温度 " + temp + "°F");
    }
}
