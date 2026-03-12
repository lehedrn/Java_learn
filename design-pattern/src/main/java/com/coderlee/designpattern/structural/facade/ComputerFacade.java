package com.coderlee.designpattern.structural.facade;

/**
 * 外观类：计算机外观
 * <p>
 * 为计算机启动提供简化的接口
 * 隐藏 CPU、内存、硬盘之间的复杂交互
 * </p>
 *
 * @author coderlee
 */
public class ComputerFacade {

    private CPU cpu;
    private Memory memory;
    private HardDrive hardDrive;

    // 引导加载程序的地址
    private static final long BOOT_ADDRESS = 0x10000000;
    private static final long BOOT_SECTOR = 0x00;
    private static final int SECTOR_SIZE = 4;

    public ComputerFacade(CPU cpu, Memory memory, HardDrive hardDrive) {
        this.cpu = cpu;
        this.memory = memory;
        this.hardDrive = hardDrive;
    }

    /**
     * 一键启动计算机
     * <p>
     * 自动完成所有启动步骤
     * </p>
     */
    public void start() {
        System.out.println("========== 计算机启动 ==========");

        // 冻结系统
        cpu.freeze();

        // 从硬盘加载引导程序到内存
        byte[] bootstrap = hardDrive.read(BOOT_SECTOR * SECTOR_SIZE);
        memory.load(BOOT_ADDRESS, bootstrap);

        // 跳转到引导程序并执行
        cpu.jump(BOOT_ADDRESS);
        cpu.execute();

        System.out.println("========== 计算机启动完成 ==========\n");
    }

    /**
     * 关闭计算机
     */
    public void shutdown() {
        System.out.println("========== 计算机关机 ==========");
        cpu.freeze();
        System.out.println("========== 已关机 ==========\n");
    }
}
