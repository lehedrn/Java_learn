package com.coderlee.designpattern.structural.adapter;

/**
 * 适配器模式演示类
 * <p>
 * 通过多个场景展示适配器模式的使用
 * </p>
 *
 * @author coderlee
 */
public class AdapterDemo {

    public static void main(String[] args) {
        System.out.println("========== 适配器模式演示 ==========\n");

        // 场景 1: 手机充电器（对象适配器）
        System.out.println("--- 场景 1: 手机充电器 ---");
        MobilePhone phone = new MobilePhone();
        Power220V power220V = new Power220V();
        Power5V adapter = new PowerAdapter(power220V);
        phone.charge(adapter);

        // 场景 2: 天气服务转换
        System.out.println("\n--- 场景 2: 天气服务转换 ---");
        WeatherApp weatherApp = new WeatherApp();
        JapanWeatherService japanService = new JapanWeatherService();
        ChinaWeatherService weatherAdapter = new WeatherAdapter(japanService);
        weatherApp.displayWeather(weatherAdapter);

        // 场景 3: 日志接口适配（缺省适配器）
        System.out.println("\n--- 场景 3: 日志接口适配 ---");
        TargetLogger logger = new ErrorLoggerAdapter();
        logger.log("这是一条测试日志");

        System.out.println("\n========== 演示结束 ==========");
    }
}
