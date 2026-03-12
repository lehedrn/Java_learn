package com.coderlee.designpattern.structural.facade;

/**
 * 第二个演示类：计算机启动
 * <p>
 * 展示外观模式如何简化计算机启动的复杂流程
 * </p>
 *
 * @author coderlee
 */
public class ComputerDemo {

    public static void main(String[] args) {
        System.out.println("========== 外观模式演示 - 计算机启动 ==========\n");

        // 创建子系统
        CPU cpu = new CPU();
        Memory memory = new Memory();
        HardDrive hardDrive = new HardDrive();

        // 创建外观类
        ComputerFacade computer = new ComputerFacade(cpu, memory, hardDrive);

        // 一键启动（无需关心底层复杂流程）
        computer.start();

        // 关机
        computer.shutdown();

        System.out.println("========== 演示结束 ==========");
    }
}
