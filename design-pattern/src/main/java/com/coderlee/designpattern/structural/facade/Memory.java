package com.coderlee.designpattern.structural.facade;

/**
 * 子系统：内存
 * <p>
 * 计算机内存
 * </p>
 *
 * @author coderlee
 */
public class Memory {

    /**
     * 加载数据
     * @param address 内存地址
     * @param data    数据
     */
    public void load(long address, byte[] data) {
        System.out.println("Memory: 加载数据到地址 " + address);
    }

    /**
     * 读取数据
     * @param address 内存地址
     * @return 数据
     */
    public byte[] read(long address) {
        System.out.println("Memory: 从地址 " + address + " 读取数据");
        return new byte[]{1, 2, 3, 4};
    }
}
