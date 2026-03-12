package com.coderlee.designpattern.structural.facade;

/**
 * 子系统：硬盘
 * <p>
 * 计算机硬盘
 * </p>
 *
 * @author coderlee
 */
public class HardDrive {

    /**
     * 读取扇区
     * @param sector 扇区号
     * @return 数据
     */
    public byte[] read(long sector) {
        System.out.println("HardDrive: 读取扇区 " + sector);
        return new byte[]{100, 101, 102, 103};
    }
}
