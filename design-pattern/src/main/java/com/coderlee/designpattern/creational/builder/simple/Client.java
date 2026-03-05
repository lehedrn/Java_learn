package com.coderlee.designpattern.creational.builder.simple;

import lombok.extern.slf4j.Slf4j;

/**
 * 客户端类 - 演示简化版建造者模式的使用
 * <p>
 * 简化版建造者模式特点：
 * 1. 使用内部静态 Builder 类，无需单独的 Builder 接口
 * 2. 通过链式调用（Fluent API）设置可选参数
 * 3. 区分必选参数和可选参数（可选参数有默认值）
 * 4. 代码更简洁，适合参数较多的复杂对象创建
 * </p>
 * <p>
 * 与经典建造者模式的对比：
 * - 经典版：适合复杂构建流程，需要 Director 控制步骤顺序
 * - 简化版：适合参数组合灵活，无需复杂构建逻辑的场景
 * </p>
 *
 * @author coderlee
 * @date 2026-03-05
 * @see Computer 产品类
 * @see Computer.Builder 内部建造者类
 */
@Slf4j
public class Client {
    /**
     * 主方法 - 演示简化版建造者模式的两种使用场景
     *
     * @param args 命令行参数 (未使用)
     */
    public static void main(String[] args) {
        // ========== 场景一：自定义配置游戏主机（链式调用，灵活配置所有参数）==========
        log.info("=========组装一台自定义游戏主机=========");
        
        // 使用链式调用方式构建游戏电脑
        // 1. 传入必选参数：CPU 和主板
        // 2. 依次调用各个 setter 方法设置可选参数
        // 3. 最后调用 build() 方法完成构建
        Computer gamingPC = new Computer.Builder("Intel Core i9-13900K", "ASUS ROG Z790")
                .ram("32GB DDR5")           // 设置内存
                .gpu("NVIDIA RTX 4090")     // 设置显卡
                .storage("2TB NVMe SSD")    // 设置存储器
                .power("850W")              // 设置电源
                .build();                   // 构建最终对象
        
        // 输出游戏电脑的配置信息
        log.info("游戏主机配置：{}", gamingPC);

        // ========== 场景二：使用默认值构建办公主机（只设必选参数）==========
        log.info("=========组装一台默认办公主机=========");
        
        // 仅传入必选参数，其他参数使用 Builder 中的默认值
        // 体现了简化版建造者模式的灵活性
        Computer officePC = new Computer.Builder("Intel Core i5-12400", "Gigabyte B660")
                .build();   // 直接使用默认配置构建
        
        // 输出办公电脑的配置信息（显示默认值）
        log.info("办公主机配置：{}", officePC);
    }
}
