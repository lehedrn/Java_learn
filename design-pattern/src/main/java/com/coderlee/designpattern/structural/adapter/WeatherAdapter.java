package com.coderlee.designpattern.structural.adapter;

/**
 * 适配器：天气服务转换器
 * <p>
 * 将日本天气服务适配为中国天气服务接口
 * 进行单位转换和语言翻译
 * </p>
 *
 * @author coderlee
 */
public class WeatherAdapter implements ChinaWeatherService {

    private JapanWeatherService japanWeatherService;

    public WeatherAdapter(JapanWeatherService japanWeatherService) {
        this.japanWeatherService = japanWeatherService;
    }

    @Override
    public double getTemperatureFahrenheit() {
        // 获取摄氏温度并转换为华氏温度
        double celsius = japanWeatherService.getTemperatureCelsius();
        double fahrenheit = celsius * 9.0 / 5.0 + 32.0;
        System.out.println("WeatherAdapter: 将 " + celsius + "°C 转换为 " + fahrenheit + "°F");
        return fahrenheit;
    }

    @Override
    public String getDescriptionChinese() {
        // 翻译日文天气描述为中文
        String japanese = japanWeatherService.getDescriptionJapanese();
        String chinese = translateJapaneseToChinese(japanese);
        System.out.println("WeatherAdapter: 将日文 '" + japanese + "' 翻译为中文 '" + chinese + "'");
        return chinese;
    }

    /**
     * 简单的日文到中文翻译（示例）
     */
    private String translateJapaneseToChinese(String japanese) {
        switch (japanese) {
            case "晴れ":
                return "晴天";
            case "曇り":
                return "多云";
            case "雨":
                return "雨天";
            case "雪":
                return "雪天";
            default:
                return "未知天气";
        }
    }
}
