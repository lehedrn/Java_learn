package com.coderlee.designpattern.creational.builder.classics;

import lombok.extern.slf4j.Slf4j;

/**
 * 客户端类 - 演示经典建造者模式的使用
 * <p>
 * 在建造者模式中，客户端负责：
 * 1. 创建指挥者对象 (Director)
 * 2. 创建具体建造者对象 (ConcreteBuilder)
 * 3. 将建造者设置到指挥者中
 * 4. 通过指挥者构建复杂对象
 * 5. 获取最终构建的产品对象
 * </p>
 *
 * @author coderlee
 * @date 2026-03-05
 * @see ComputerDirector 指挥者类
 * @see ComputerBuilder 抽象建造者接口
 * @see GamingComputerBuilder 具体建造者 - 游戏电脑
 * @see OfficeComputerBuilder 具体建造者 - 办公电脑
 */
@Slf4j
public class Client {
    /**
     * 主方法 - 演示建造者模式的完整使用流程
     *
     * @param args 命令行参数 (未使用)
     */
    public static void main(String[] args) {
        // 创建指挥者对象，负责控制构建流程
        ComputerDirector director = new ComputerDirector();

        // ========== 第一部分：构建游戏电脑 ==========
        log.info("=========组装一台游戏主机=========");
        
        // 创建游戏电脑建造者（具体建造者）
        GamingComputerBuilder gamingComputerBuilder = new GamingComputerBuilder();
        
        // 将建造者设置到指挥者中
        director.setBuilder(gamingComputerBuilder);
        
        // 指挥者控制建造过程，按顺序构建各个部件
        director.constructComputer();
        
        // 从指挥者获取构建完成的电脑产品
        Computer gamingPC = director.getComputer();
        
        // 输出游戏电脑的配置信息
        log.info("生成的游戏主机配置：{}", gamingPC);

        // ========== 第二部分：构建办公电脑 ==========
        log.info("=========组装一台办公主机=========");
        
        // 创建办公电脑建造者（另一个具体建造者）
        OfficeComputerBuilder officeComputerBuilder = new OfficeComputerBuilder();
        
        // 更换指挥者中的建造者
        director.setBuilder(officeComputerBuilder);
        
        // 再次执行构建流程（使用新的建造者）
        director.constructComputer();
        
        // 获取办公电脑产品
        Computer officePC = director.getComputer();
        
        // 输出办公电脑的配置信息
        log.info("生成的办公室主机配置：{}", officePC);
    }
}
