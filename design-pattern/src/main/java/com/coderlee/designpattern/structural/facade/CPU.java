package com.coderlee.designpattern.structural.facade;

/**
 * 子系统：CPU
 * <p>
 * 计算机 CPU
 * </p>
 *
 * @author coderlee
 */
public class CPU {

    /**
     * 冻结系统
     */
    public void freeze() {
        System.out.println("CPU: 系统冻结");
    }

    /**
     * 跳转执行
     * @param address 内存地址
     */
    public void jump(long address) {
        System.out.println("CPU: 跳转到内存地址 " + address);
    }

    /**
     * 执行指令
     */
    public void execute() {
        System.out.println("CPU: 执行指令");
    }
}
